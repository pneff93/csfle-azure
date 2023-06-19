# Client Side Field Level Encryption with Azure


## Prerequisites

* Confluent Cloud cluster with Advanced Stream Governance package


## Create Tag

We first need to create a tag on which we apply the encryption later, such as `PII`.
As of today, we need to create the tag in the Stream Catalog first, see the [documentation](https://docs.confluent.io/platform/current/schema-registry/fundamentals/data-contracts.html#tags) of Data Contracts.

## Register Schema

We register the schema with setting `PII` to the SSN field and defining the encryption rule

```shell
curl --request POST --url 'https://psrc-4kk0p.westeurope.azure.confluent.cloud/subjects/pneff-csfle-test-value/versions'   \
  --header 'Authorization: Basic RFVaWU1GTktRT01FWVo2STptSjE2YWdldVQwU2ZYYS94cVIwTXVSKzlzaXhsTXVoZ25rUi90MVR4MzhYRldlNXd6WkRuN3FCdmNIV2x1MWFm' \
  --header 'content-type: application/octet-stream' \
  --data '{
            "schemaType": "AVRO",
            "schema": "{  \"name\": \"PersonalData\", \"type\": \"record\", \"namespace\": \"com.csfleExample\", \"fields\": [{\"name\": \"id200\", \"type\": \"string\"}, {\"name\": \"name\", \"type\": \"string\"},{\"name\": \"SSN\", \"type\": \"float\", \"confluent:tags\": [ \"PII\"]},{\"name\": \"timestamp\",\"type\": [\"string\", \"null\"]}]}",
            "metadata": {
            "properties": {
            "owner": "Patrick Neff",
            "email": "pneff@confluent.io"
            },
        "ruleSet": {
        "domainRules": [
            {
            "name": "encryptPII",
            "kind": "ENCRYPT",
            "tags": ["PII"],
            "onFailure": "None,None"
            }
            ]
        } 
        }
    }' 
```

## Azure Key Vault

We need to create a Key Vault in Azure and a key.
Copy the Key Identifier

![](Azure.png)

## Producer/Consumer configuration

We need to adjust the configuration by adding
```kotlin
// Encryption
settings.setProperty("rule.executors", "encryptPII")
settings.setProperty("rule.executors.encryptPII.class", "io.confluent.kafka.schemaregistry.encryption.local.LocalFieldEncryptionExecutor")
settings.setProperty("rule.executors.encryptPII.param.default.kms.key.id", "https://xxxx.vault.azure.net/keys/key1/xxxx") <-- Key Identifier

// Required since we manually create schemas
settings.setProperty("use.latest.version", "true")
settings.setProperty("auto.register.schemas","false")
```

## Produce

We continuously produce data with the encryption
```
./gradlew run
```

## Consume

### without encryption

## with encryption
