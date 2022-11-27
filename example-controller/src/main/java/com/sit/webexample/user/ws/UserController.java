package com.sit.webexample.user.ws;

import com.sit.webexample.user.entity.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 框架测试-测试对象属性值自动检测机制
 */
@RestController(value = "prettyUserController")
@RequestMapping("/pretty")
@Slf4j
public class UserController {

    /**
     * http://127.0.0.1:12007/pretty/test-validation/v1
     * @param userDTO
     * @return
     */
    @PostMapping("/test-validation/v1")
    public UserDTO testValidation(@RequestBody @Validated UserDTO userDTO) {
        log.info("userDTO is value:{}", userDTO);
        return userDTO;
    }

    /**
     * http://127.0.0.1:12007/pretty/testchar/v1
     * @return
     */
    @GetMapping("/testchar/v1")
    public String testCharResponse(){
        return "hello word";
    }

    /**
     * http://127.0.0.1:12007/pretty/test-validation/v2
     * @param userDTO
     * @return
     */
    @PostMapping("/test-validation/v2")
    public List testValidations(@RequestBody @Validated UserDTO userDTO) {
        log.info("userDTO is value:{}", userDTO);
        List tempRes = new ArrayList(2);
        tempRes.add(userDTO);
        return tempRes;
    }

    /**
     * http://127.0.0.1:12007/pretty/testchar/v3
     * @return
     */
    @GetMapping("/testchar/v3")
    public String testException(){
        Integer.parseInt("asdqwe");
        return "hello word";
    }

}
