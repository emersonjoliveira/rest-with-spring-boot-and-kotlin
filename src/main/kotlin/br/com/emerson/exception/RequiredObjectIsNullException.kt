package br.com.emerson.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(HttpStatus.BAD_REQUEST)
class RequiredObjectIsNullException: RuntimeException {

    constructor():super("It's not allowed to persist a null object")
    constructor(exception: String?):super(exception)
}