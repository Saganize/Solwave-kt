package com.saganize.solwave.domain.model

import androidx.annotation.Keep
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.solana.core.Transaction
import kotlinx.serialization.Serializable
import java.lang.reflect.Type

@Keep
@Serializable
class TransactionSerializer : JsonSerializer<Transaction> {
    override fun serialize(
        src: Transaction,
        typeOfSrc: Type,
        context: JsonSerializationContext,
    ): JsonElement {
        val jsonObject = JsonObject()

        // Serialize signatures
//        val signaturesArray = JsonArray()
//        src.signatures.forEach { signaturePubkeyPair ->
//            val signatureObject = JsonObject()
//            signatureObject.addProperty("publicKey", signaturePubkeyPair.publicKey.toString())
//            signaturePubkeyPair.signature?.let {
//                signatureObject.addProperty("signature", Base64.encodeToString(it, Base64.NO_WRAP))
//            }
//            signaturesArray.add(signatureObject)
//        }
//        jsonObject.add("signatures", signaturesArray)

        // Serialize other fields
//        jsonObject.addProperty("recentBlockhash", src.recentBlockhash)

        // Serialize instructions
        val instructionsArray = JsonArray()
        src.instructions.forEach { instruction ->
            val instructionObject = JsonObject()
            val keysArray = JsonArray()
            instruction.keys.forEach { accountMeta ->
                val keyObject = JsonObject()
                keyObject.addProperty("pubkey", accountMeta.publicKey.toString())
                keyObject.addProperty("isSigner", accountMeta.isSigner)
                keyObject.addProperty("isWritable", accountMeta.isWritable)
                keysArray.add(keyObject)
            }
            instructionObject.add("keys", keysArray)
            instructionObject.addProperty("programId", instruction.programId.toString())

            val dataArray = JsonArray()
            instruction.data.forEach { byte ->
                // Kotlin Byte is signed, so we need to convert it to an unsigned int
                dataArray.add((byte.toInt() and 0xFF))
            }
            // Add the JsonArray to the 'instructionObject'
            instructionObject.add("data", dataArray)

//            instructionObject.addProperty(
//                "data",
//                instruction.data.contentToString()
//            )
            instructionsArray.add(instructionObject)
        }
        jsonObject.add("instructions", instructionsArray)

        src.feePayer?.let { jsonObject.addProperty("feePayer", it.toString()) }

        // Serialize nonceInfo if present
        src.nonceInfo?.let { nonceInfo ->
            val nonceInfoObject = JsonObject()
            nonceInfoObject.addProperty("nonce", nonceInfo.nonce)
            nonceInfoObject.add("nonceInstruction", context.serialize(nonceInfo.nonceInstruction))
            jsonObject.add("nonceInfo", nonceInfoObject)
        }

        return jsonObject
    }
}
