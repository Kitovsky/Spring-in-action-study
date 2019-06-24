package kit.flow

import org.springframework.integration.annotation.MessagingGateway
import org.springframework.integration.file.FileHeaders
import org.springframework.messaging.handler.annotation.Header

@MessagingGateway(defaultRequestChannel = "textInputChannel")
interface FileWriterGateway {
    fun write2File(@Header(FileHeaders.FILENAME) fileName: String,
                   data: String)
}