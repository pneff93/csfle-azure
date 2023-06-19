import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.config.SaslConfigs
import java.util.*

class ProducerProperties {

    fun configureProperties() : Properties{

        val settings = Properties()
        settings.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
        settings.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaAvroSerializer")

        val api_key = "3IIANOT4DTRK2IWY"
        val api_secret = "51cSM05S0bbkx7B77Vl5O5k4mByBpUB4wkoFfwq74nHE43YWln7daIbFvVKHe1DK"
        val broker = "pkc-w7d6j.germanywestcentral.azure.confluent.cloud:9092"


        settings.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "$broker")
        settings.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL")
        settings.setProperty(SaslConfigs.SASL_MECHANISM, "PLAIN")

        settings.setProperty(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required username='$api_key' password='$api_secret';")

        settings.setProperty("schema.registry.url", "https://psrc-4kk0p.westeurope.azure.confluent.cloud")
        settings.setProperty("basic.auth.credentials.source", "USER_INFO")
        settings.setProperty("schema.registry.basic.auth.user.info", "DUZYMFNKQOMEYZ6I:mJ16ageuT0SfXa/xqR0MuR+9sixlMuhgnkR/t1Tx38XFWe5wzZDn7qBvcHWlu1af")

        // Encryption
        settings.setProperty("rule.executors", "encryptPII")
        settings.setProperty("rule.executors.encryptPII.class", "io.confluent.kafka.schemaregistry.encryption.local.LocalFieldEncryptionExecutor")
        settings.setProperty("rule.executors.encryptPII.param.default.kms.key.id", "https://pneff-test.vault.azure.net/keys/pneff-BYOK/f6cfdb03d0354315900a377f9650a25b")

        // Required since we manually create schemas
        settings.setProperty("use.latest.version", "true")
        settings.setProperty("auto.register.schemas","false")


        return settings
    }
}