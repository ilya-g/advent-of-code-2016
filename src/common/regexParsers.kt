package common.parsers


class RegexParser<out T>(val pattern: Regex, val parser: MatchParser<T>)
typealias GroupParser<T> = (String) -> T
typealias MatchParser<T> = (MatchResult) -> T

fun <T, R> groups(p1: GroupParser<T>, f: (T) -> R): MatchParser<R> =
        { it.destructured.let { (g1) -> f(p1(g1)) } }
fun <T1, T2, R> groups(p1: GroupParser<T1>, p2: GroupParser<T2>, f: (T1, T2) -> R): MatchParser<R> =
        { it.destructured.let { (g1, g2) -> f(p1(g1), p2(g2)) } }
fun <T1, T2, T3, R> groups(p1: GroupParser<T1>, p2: GroupParser<T2>, p3: GroupParser<T3>, f: (T1, T2, T3) -> R): MatchParser<R> =
        { it.destructured.let { (g1, g2, g3) -> f(p1(g1), p2(g2), p3(g3)) } }
fun <T1, T2, T3, T4, R> groups(p1: GroupParser<T1>, p2: GroupParser<T2>, p3: GroupParser<T3>, p4: GroupParser<T4>, f: (T1, T2, T3, T4) -> R): MatchParser<R> =
        { it.destructured.let { (g1, g2, g3, g4) -> f(p1(g1), p2(g2), p3(g3), p4(g4)) } }


class RegexParsers<out T>(val parsers: Sequence<RegexParser<T>>) {
    fun parse(input: String): T {
        parsers.forEach { it.pattern.matchEntire(input)?.let { m -> return it.parser(m) } }
        error("No pattern matched for string '$input'")
    }
    fun parseOrNull(input: String): T? {
        parsers.forEach { it.pattern.matchEntire(input)?.let { m -> return it.parser(m) } }
        return null
    }
}

class RegexParsersBuilder<T> {
    private val parsers = mutableListOf<RegexParser<T>>()

    fun <R> groupParser(f: GroupParser<R>): GroupParser<R> = f

    fun pattern(pattern: String, parser: MatchParser<T>) {
        parsers += RegexParser(Regex(pattern), parser)
    }
    infix fun Regex.onMatch(parser: MatchParser<T>) {
        parsers += RegexParser(this, parser)
    }

    fun build() = RegexParsers(parsers.asSequence())
}


fun <T: Any> regexParsers(builder: RegexParsersBuilder<T>.() -> Unit): RegexParsers<T> =
        RegexParsersBuilder<T>().apply(builder).build()

