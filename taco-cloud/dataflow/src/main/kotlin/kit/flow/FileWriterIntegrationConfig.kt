package kit.flow

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportResource
import org.springframework.context.annotation.Profile
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.annotation.Transformer
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.dsl.MessageChannels
import org.springframework.integration.file.FileWritingMessageHandler
import org.springframework.integration.file.dsl.Files
import org.springframework.integration.file.support.FileExistsMode
import org.springframework.integration.transformer.GenericTransformer
import java.io.File

@Configuration
class FileWriterIntegrationConfig {

    val filePath = "/output/result"

    @Profile("javaconfig")
    @Bean
    @Transformer(inputChannel = "textInputChannel",
            outputChannel = "fileOutputChannel")
    fun upperCaseTransformer(): GenericTransformer<String, String> = GenericTransformer(String::toUpperCase)

    @Profile("javaconfig")
    @Bean
    @ServiceActivator(inputChannel = "fileOutputChannel")
    fun fileWriter(): FileWritingMessageHandler = FileWritingMessageHandler(File(filePath)).apply {
        setExpectReply(false)
        setFileExistsMode(FileExistsMode.APPEND)
        setAppendNewLine(true)
    }

    @Profile("javadsl")
    @Bean
    fun fileWriterFlow(): IntegrationFlow = IntegrationFlows
            .from(MessageChannels.direct("textInputChannel"))
            .transform(String::toUpperCase)
            .handle(Files
                    .outboundAdapter(File(filePath))
                    .fileExistsMode(FileExistsMode.APPEND)
                    .appendNewLine(true))
            .get()

}

@Profile("xmlconfig")
@Configuration
@ImportResource("classpath:/filewriter-config.xml")
class XmlConfig
