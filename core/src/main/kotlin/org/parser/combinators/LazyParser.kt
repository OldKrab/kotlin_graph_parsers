package org.parser.combinators

import org.parser.sppf.NonPackedNode
import org.parser.sppf.SPPFStorage
import kotlin.reflect.KProperty

/** [LazyParser] represent parser with late initialization. Useful when need create recursive parser.
 *
 *  Real parser should be written into [p] field. */
class LazyParser<InS, OutS, R> : BaseParser<InS, OutS, R>(false, "lazy") {
    lateinit var p: BaseParser<InS, OutS, R>

    override var view: String = "lazy"
        get() = if (::p.isInitialized) p.view else field
        set(value) {
            field = value
        }

    override val isMemoized: Boolean
        get() = if (::p.isInitialized) p.isMemoized else false

    override fun parse(sppf: SPPFStorage, inS: InS): ParserResult<NonPackedNode<InS, OutS, R>> {
        if (!this::p.isInitialized)
            throw UninitializedPropertyAccessException("Parser not initialized")
        return p.parse(sppf, inS)
    }

    override operator fun getValue(r: Any?, property: KProperty<*>): LazyParser<InS, OutS, R> {
        return this
    }

    override operator fun provideDelegate(thisRef: Any?, property: KProperty<*>): LazyParser<InS, OutS, R> {
        this.view = property.name
        return this
    }
}
