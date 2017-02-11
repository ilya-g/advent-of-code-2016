package common.md5

val md5 = java.security.MessageDigest.getInstance("MD5")
fun md5(s: String): ByteArray = md5.digest(s.toByteArray())
fun md5hex(s: String): String = md5(s).toHexString()
fun ByteArray.toHexString() = joinToString("") { (it.toInt() and 0xFF).toString(16).padStart(2, '0') }


