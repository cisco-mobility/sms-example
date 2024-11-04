package com.cisco.mobility.api.demo

import build.buf.gen.cisco.mobility.sms.v1.SmsServiceGrpc
import com.wgtwo.auth.WgtwoAuth
import com.wgtwo.auth.model.Token
import io.grpc.ManagedChannelBuilder

class GrpcUtils(private val config: Config) {

    private val channel = ManagedChannelBuilder.forTarget(config.apiTarget).build()
    private val smsBlockingStub: SmsServiceGrpc.SmsServiceBlockingStub = SmsServiceGrpc.newBlockingStub(channel)

    fun getSmsBlockingStub() = smsBlockingStub

    fun getToken(): Token {
        val wg2Auth = WgtwoAuth.builder(config.apiClientId, config.apiClientSecret).build()
        val scope = config.requiredScopes.joinToString(" ")
        return wg2Auth.clientCredentials.fetchToken(scope)
    }

}
