package prototype;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ImplementationVisibility;

@Style(

        /** Detect names starting with underscore */
        typeAbstract = "_*",

        /** Generate without any suffix, just raw detected name */
        typeImmutable = "*",

        /** Make generated public, leave underscored as package private */
        visibility = ImplementationVisibility.PUBLIC,

        /** Seems unnecessary to have builder or superfluous copy method */
        defaults = @Immutable(builder = false, copy = false, intern = false, prehash = true))
public @interface Wrapped {
}
