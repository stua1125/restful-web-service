package com.kakaopay.restfulwebservice.helloworld;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController // 반환되는 값이 response body가 자동으로 json format으로 return해준다.
public class HelloWorldController {

    // 전송방식: GET
    // url: hello-world (endpoint)
    // 이전 사용 어노테이션 @RequestMapping(method-Request.GET, path="/hello-world") -> @GetMapping

    @GetMapping("/hello-world") // path
    public String helloWorld (){
        return "Hello World";
    }

    @GetMapping("/hello-world-bean")
    public HelloWorldBean helloWorldBean (){
        return new HelloWorldBean("Hello World");
    }

    @GetMapping("/hello-world-bean/path-variable/{name}")
    public HelloWorldBean helloWorldBean (@PathVariable String name){
        return new HelloWorldBean(String.format("Hello World, %s" , name));
    }
}
