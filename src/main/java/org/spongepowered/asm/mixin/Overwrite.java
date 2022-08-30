/*
 * This file is part of Mixin, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.asm.mixin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//import org.spongepowered.asm.util.ConstraintParser.Constraint;

/**
 * <p>Annotation used to indicate a mixin class member which must overwrite a
 * method in the target class.</p>
 * 
 * <p>The default behaviour of mixin classes when merging mixin methods is to
 * replace methods in the target class which already exist, and simply add any
 * other methods to the target class body as new members. This default behaviour
 * allows methods in the target class to be easily overwritten by simply
 * creating a method in the mixin with a signature matching the member to be
 * overwritten.</p>
 * 
 * <p>This is not sufficient for obfuscated methods however, since as mixins
 * traverse the obfuscation boundary, this association with the target method is
 * lost because the method name will change. The {@link Overwrite} annotation is
 * used to indicate to the annotation processor that this method is intended to
 * overwrite a member in the target class, and should be added to the
 * obfuscation table if {@link #remap} is true.</p>
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Overwrite {
    
    /**
     * Returns constraints which must be validated for this overwrite to
     * succeed. See {@link Constraint} for details of constraint formats.
     * 
     * @return Constraints for this annotation
     */
    public String constraints() default "";
    
    /**
     * Supplies possible aliases for this method. This should <b>only</b>
     * be used in the following scenarios:
     * 
     * <ul>
     *   <li>When shadowing a sythetic method which can have differen names at
     *   development time because it is regenerated by the compiler.
     *   </li>
     *   <li>When another mod or transformer is known to change the name of a
     *   field</li>
     * </ul>
     * 
     * @return Aliases for this member
     */
    public String[] aliases() default { }; 

    /**
     * By default, the annotation processor will attempt to locate an
     * obfuscation mapping for all {@link Overwrite} methods since it is
     * anticipated that in general the target of a {@link Overwrite} annotation
     * will be an obfuscated method in the target class. However since it is
     * possible to also overwrite methods in non-obfuscated targets it may be
     * necessary to suppress the compiler error which would otherwise be
     * generated. Setting this value to <em>false</em> will cause the annotation
     * processor to skip this annotation when attempting to build the
     * obfuscation table for the mixin.
     * 
     * @return True to instruct the annotation processor to search for
     *      obfuscation mappings for this method 
     */
    public boolean remap() default true;

}