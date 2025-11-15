package com.flow.blockext.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class RootRedirectController {

    @GetMapping("/")
    fun redirect(): String {
        return "redirect:/swagger-ui/index.html"
    }
}