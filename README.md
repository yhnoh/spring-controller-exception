# spring-controller-exception

### 목표
스프링이나 스프링부트 사용하면서 우리는 많은 예외를 발생시킨다.
- 스프링 시큐리티에서 잡아주는 에러 핸들러
- JDBC 트랜잭션에 대한, Rollback 예외
- AOP를 이용한 예외처리

위에서 제시한 방법 말고 다양한 방법이 존재하지만, 위에서 제시하는
많은 방법들은 전부다 **"공통로직"과 "비지니스로직"을 분리**한다는 이점이 있다.

즉 비지니스 로직을 작성하는 개발자는 RuntimeException 또는 Exception을 이용하여
여러 예외를 처리하고자 이를 공통으로 해결해 줄수 있는 해결사같은 역할을 한다는 것이다.

실무에서 작업을 진행할때 무수히 많은 try-catch문으로 인해서 
많은 고역을 느낀 경험이 있을 것이다.
뿐만 아니라 오늘 이야기 하고자 하는 controller에 대한
expcetion을 처리하고자 할때 잘못하면 사용자에게 잘못된
데이터를 응답해 줄 경우도 발생할 수 있으며 응답 방시이 달라질때 마다 프론트 개발자가 화가 날 수 도 있다.

### 해결 키워드
@ResponseStatus, @ExceptionHandler, @RestControllerAdvice, 다형성

### 일반적인 예외발생시
```java
@RestController
@RequiredArgsConstructor
@Slf4j
public class ExceptionController {
    @GetMapping(value = "/exception")
    public void exception() throws Exception {
        throw new Exception("Exception 에러 발생");
    }

    @GetMapping(value = "/runtime-exception")
    public void runtimeException() {
        throw new RuntimeException("RuntimeException 에러 발생");
    }
}
```

만약 위와같은 방식으로 예외처리를 진행했다고 가정해보자.
우리는 클라이언트에게 예외가 발생하여도 다양한 데이터를 내려준다. 
**예를 들어 에러가 발생했으니 이러한 데이터를 받으시고 메시지도 받으세요 
또는 5XX에러가 발생하였여도 우리는 다른 사용자들에게 다른 4XX에러를 내려주고자 할수도 있다.**
다른 방법이 없을까?

### @ResponseStatus
```java

@RestController
@RequiredArgsConstructor
@Slf4j
public class ExceptionController {
    @GetMapping(value = "/annotation-custom-exception")
    public void annotationCustomException() {
        throw new AnnotationCustomException("RuntimeException 에러 발생");
    }
}

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "AnnotationCustomException 입니다.")
public class AnnotationCustomException extends RuntimeException{

    public AnnotationCustomException(String message) {
        super(message);
    }
}
```

@ReponseStatus을 이용하여 좀더 우리가 의도한 status나 메시지를 작성하여 전달할 수 있다.
뿐만 아니라 비지니스로직을 작성하는 사람이 저 에러만 발생시키면 동일한 데이터를 전달할 것이다.
하지만 여기서 **큰 단점은 동일한 status 동일한 메시지를 계속해서 전달**한다는 것이다.
다른 메세지, 다른 status를 전달하고자 할때마다 클래스를 만드는 것도 좋은 방법은 아니다.

간단하게는 사용할 수 있지만, 다양한 상황에 대한 대처가 안되니 참으로 난감하다.
다른 방법은 없을까?

### @ExceptionHandler
```java
@RestController
@RequiredArgsConstructor
@Slf4j
public class ExceptionController {
    @GetMapping(value = "/exception-handler")
    public void exceptionHandler(){
        throw new IllegalArgumentException("IllegalArgumentException 에러 발생");
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    private ResponseEntity illegalArgumentExceptionHandler(IllegalArgumentException e){
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    
}
```

@ExceptionHandler를 이용하면, 해당 컨트롤러 내부에서 사용자가 지정한 에러에
대해서 핸들링을 해준다. 또한 value라는 옵션을 이용하여 하나의 예외가 아닌 
많은 에러에 대한 핸들링이 @ExceptionHandler내부에서 가능하다. 응답 또한 사용자가 원하는 메시지나
데이터를 설정하여 전달하 수 도 있다.

정말 괜찮은 방식이기는 하나 우리가 그러면 컨트롤러 마다 다 적어주어야하나?
또한 모든 사용자가 지정한 에러에 대해서 동일 응답을 내려주고 싶다면 어떻게 해야할까?

### @ControllerAdvice, @RestControllerAdvice, @ExceptionHandler

```java
@RestController
@RequiredArgsConstructor
@Slf4j
public class ExceptionController {
    @GetMapping(value = "/controller-advice")
    private void controllerAdvice(){
        throw new CustomException("에러코드", "에러메시지");
    }
}

@RestControllerAdvice
@Slf4j
public class CustomControllerAdvice {
    @ExceptionHandler(value = CustomException.class)
    protected ResponseEntity<CustomExceptionResponse> customException(CustomException e){
        log.error("error_code = {}, error_msg = {}", e.getError_code(), e.getMsg());
        return CustomExceptionResponse.responseEntity(HttpStatus.NOT_FOUND, e.getError_code(), e.getMsg());
    }    
}

@Getter
public class CustomException extends RuntimeException{
    String error_code = "";
    String msg = "";

    public CustomException() {
        super();
    }

    public CustomException(String message) {
        super(message);
        this.msg = message;
    }

    public CustomException(String error_code, String message) {
        super(message);
        this.error_code = error_code;
        this.msg = message;
    }

}

@Getter
public class CustomExceptionResponse {

    private LocalDateTime now = LocalDateTime.now();
    private boolean result = false;
    private String error_code;
    private String msg;


    public static ResponseEntity<CustomExceptionResponse> responseEntity(HttpStatus status, String error_code, String msg){
        CustomExceptionResponse customExceptionResponse = new CustomExceptionResponse();
        customExceptionResponse.error_code = error_code;
        customExceptionResponse.msg = msg;

        return ResponseEntity.status(status).body(customExceptionResponse);
    }
}

```

@RestControllerAdvice를 이용하여 전체 컨트롤러에 대한 예외 핸들링이 가능하다.
뿐만아니라 CustomExceptionResponse을 이용하여 동일 응답을 보장하고, @ExceptionHandler에
CustomException을 잡아달라고 지정해 두었으니 사용자는 `throw new CustomException`을
통해서 따로 응답을 어떻게 내려줘야할지 크게 고민할 필요가 없어짐으로
"공통 로직"과 "비지니스 로직"을 분리시키는게 가능해 졌다.

그러나 개발자들은 한 에러만 사용하지 않는다. 에러 클래스명에 대한 정의가
더 잘되어 있으면 그 예외를 찾기에 더 수월하기 때문에 다양한 예외를 사용한다.
우리는 이를 **다형성**으로 해결할 수 있다.

### 다형성
```java
@RestController
@RequiredArgsConstructor
@Slf4j
public class ExceptionController {
    @GetMapping(value = "/polymorphism-controller-advice")
    private void polymorphismControllerAdvice(){
        throw new CustomChildException("자식 에러코드", "자식 에러메시지");
    }
}

public class CustomChildException extends CustomException{

    public CustomChildException() {
        super();
    }

    public CustomChildException(String message) {
        super(message);
    }

    public CustomChildException(String error_code, String message) {
        super(error_code, message);
    }
}
```

상위 클래스는 하위 클래스에 대해 참조가 가능하다. 위와 같이 상속을
받게 되면서 개발자는 다양한 예외를 작성하면서 동시에 응답에 대해 공통으로
처리가 가능해진다.


### 마치며...
생각보다 예외에 대해 어떻게 핸들링해야하는지 모르게 되면 많은 개발자들이
다양한 응답처리를 한다. 한마디로 표준이 없다는 것이다. 응답을 처리하는
개발자들은 굉장히 당혹스럽고 힘든 경험을 하게 될것이다. 위와 같은 에러핸들링에 대한
학습은 굉장히 중요하다. 뿐만 아니라 공통로직과 비지니스로직을 분리하면서 사용가능하기
때문에 실무에서 생각보다 활용할 수 있는 방법이 많을것 같다.

### 궁금점?
만약 컨트롤러 내부에 @ExceptionHandler가 있고 글로벌한 @ControllerAdvice가
존재하면서 동시에 동일 에러를 핸들링하고 있으면 누가 먼저할까?
바로 컨트롤러 내부에 있는 @ExceptionHandler가 에러 핸들링을 한다.

### 출처
* https://bcp0109.tistory.com/303
* https://incheol-jung.gitbook.io/docs/q-and-a/spring/controlleradvice-exceptionhandler




















