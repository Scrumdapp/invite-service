package com.scrumdapp.inviteservice.exceptions

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ExceptionResponse(val code: Int, val message: String?)