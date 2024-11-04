package com.cisco.mobility.api.demo

import build.buf.gen.cisco.mobility.sms.v1.SendTextMessageResponse
import build.buf.gen.cisco.mobility.sms.v1.SmsServiceGrpc.SmsServiceBlockingStub
import build.buf.gen.cisco.mobility.sms.v1.sendTextMessageRequest
import com.google.protobuf.value
import com.wgtwo.auth.BearerTokenCallCredentials
import com.wgtwo.auth.model.Token
import io.grpc.Metadata
import io.grpc.stub.MetadataUtils

class SmsService(
    private val stub: SmsServiceBlockingStub,
    private val token: Token
) {

    fun sendSms(
        request: SmsRequest,
    ): SendTextMessageResponse {
        val sendTextMessageRequest = sendTextMessageRequest {
            when (request.recipient) {
                is MsisdnSmsRecipient -> msisdn = request.recipient.getValue()
                is IccidSmsRecipient -> iccid = request.recipient.getValue()
            }
            request.fromAddress?.let {
                fromAddress = request.fromAddress
            }
            content = request.content
        }

        val requestHeaders = getHeaders(request)
        val response = stub
            .withCallCredentials(BearerTokenCallCredentials { token.accessToken })
            .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(requestHeaders))
            .sendTextMessage(sendTextMessageRequest)
        return response
    }

    private fun getHeaders(request: SmsRequest): Metadata {
        val networkIdentifier = request.recipient.getHeader()
        val headers = Metadata()
        headers.put(HEADER_CISCO_NETWORK_IDENTIFIER, networkIdentifier)
        return headers
    }

    companion object {
        private val HEADER_CISCO_NETWORK_IDENTIFIER =
            Metadata.Key.of(
                "Cisco-Network-Identifier",
                Metadata.ASCII_STRING_MARSHALLER,
            )
    }

}

data class SmsRequest(
    val recipient: SmsRecipient,
    val fromAddress: String?,
    val content: String,
)

interface SmsRecipient {
    fun getValue(): String
    fun getHeader(): String
}

class IccidSmsRecipient(private val iccid: String) : SmsRecipient {
    override fun getValue() = iccid
    override fun getHeader() = "iccid:$iccid"
    override fun toString() = getHeader()
}

class MsisdnSmsRecipient(private val msisdn: String) : SmsRecipient {
    override fun getValue() = msisdn
    override fun getHeader() = msisdn.removePrefix("+").let { "msisdn:$it" }
    override fun toString() = getHeader()
}
