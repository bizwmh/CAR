/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;

import biz.car.SYS;
import biz.car.XRuntimeException;
import biz.car.bundle.MSG;

/**
 * Utility methods for handling field injection.
 */
public class XField implements Consumer<Field> {

	/**
	 * Searches the field named {@code aName} in the given {@code aObjectClass}. If
	 * the target class has no acceptable field the class hierarchy is traversed
	 * until a field is found or the root of the class hierarchy is reached without
	 * finding a field.
	 * <p>
	 * If an unexpected error occurs while searching or if no field is found,
	 * {@code null} is returned. If the field is found, but not usable (e.g. due to
	 * visibility restrictions), {@code XField#isUsable} will return {@code false}.
	 *
	 * @param aClass The target class of the field
	 * @param aName  The name of the field
	 * @return A <code>XField</code> instance or {@code null} if the an unexpected
	 *         error occurred or if the field was not found
	 */
	public static XField forField(Class<?> aClass, String aName) {
		XField l_ret = new XField(aClass);
		Field l_field = l_ret.searchField(aClass, aName);

		if (l_field != null) {
			l_ret.accept(l_field);

			return l_ret;
		}
		return null;
	}

	private boolean acceptPackage;
	private boolean acceptPrivate;
	private boolean bStatic;
	private Field field;
	private Class<?> theClazz;
	private ClassLoader theClazzLoader;
	private String thePackage;
	private boolean usable;

	/**
	 * Creates a <code>XField</code> instance for the given class.
	 * 
	 * @param aClass the class holding the field
	 */
	private XField(Class<?> aClass) {
		super();

		theClazz = aClass;
		theClazzLoader = theClazz.getClassLoader();
		thePackage = ClassUtil.getPackageName(theClazz);
		acceptPackage = true;
		usable = true;
		bStatic = false;
		acceptPrivate = true;
	}

	@Override
	public void accept(Field aField) {
		// save field in this XField instance
		field = aField;
		// check modifiers now
		int l_mod = field.getModifiers();
		bStatic = Modifier.isStatic(l_mod);

		// accept public and protected fields
		if (Modifier.isPublic(l_mod) || Modifier.isProtected(l_mod)) {
			setAccessible();
			return;
		}
		// accept private if accepted
		if (Modifier.isPrivate(l_mod)) {
			if (acceptPrivate) {
				setAccessible();
				return;
			}
		} else {
			// accept default (package)
			if (acceptPackage) {
				setAccessible();
				return;
			}
		}
		usable = false;
	}

	/**
	 * @return <code>true</code> if the field is static
	 */
	public boolean isStatic() {
		return bStatic;
	}

	/**
	 * @return returns <code>false</code> if the field was found but is not usable,
	 *         i.g. due to visability.
	 */
	public boolean isUsable() {
		return usable;
	}

	/**
	 * Set the field for the given object.
	 * 
	 * @param aObject the object instance where to set the value
	 * @param aValue  The value to set
	 */
	public void setValue(final Object aObject, final Object aValue) {
		if (!usable) {
			SYS.LOG.error(MSG.FIELD_NOT_USABLE, field.getName());
			throw new XRuntimeException(MSG.FIELD_NOT_USABLE, field.getName());
		}
		try {
			field.set(aObject, aValue);
		} catch (final Exception anEx) {
			SYS.LOG.error(MSG.FIELD_CANT_BE_SET, field.getName());

			throw new XRuntimeException(anEx);
		}
	}

	/**
	 * Return a string representation of the field.<br>
	 * declaringClass::objectClass:field
	 * 
	 * @return A string representation of the field
	 */
	@Override
	public String toString() {
		StringBuffer l_str = new StringBuffer();
		String l_declaring = field.getDeclaringClass().getName();
		String l_clazz = theClazz.getName();

		if (!l_clazz.equals(l_declaring)) {
			l_str.append(l_declaring);
			l_str.append("::"); //$NON-NLS-1$
		}
		l_str.append(l_clazz);
		l_str.append("::"); //$NON-NLS-1$
		l_str.append(field.getName());

		return l_str.toString();
	}

	/**
	 * Finds the field named {@code aName} in the given {@code aClass}.
	 * 
	 * @param aClass the class where to look for the field
	 * @param aName  the name of the field
	 * @return the <code>Field</code> instance found or <code>null</code>
	 */
	private Field getField(Class<?> aClass, String aName) {
		Field l_ret = null;

		try {
			// find the declared field in the given class
			l_ret = aClass.getDeclaredField(aName);
		} catch (NoSuchFieldException anEx) {
			SYS.LOG.debug(MSG.DECLARED_FIELD_NOT_FOUND, aName, aClass.getName());
		} catch (Exception anEx) {
			throw SYS.LOG.exception(anEx);
		}
		return l_ret;
	}

	private Class<?> getSuperClass(Class<?> aClass) {
		Class<?> l_ret = aClass.getSuperclass();

		if (l_ret != null) {
			acceptPrivate = false;
			acceptPackage &= l_ret.getClassLoader() == theClazzLoader
			      && thePackage.equals(ClassUtil.getPackageName(l_ret));
		}
		return l_ret;
	}

	/**
	 * Finds the field named {@code aName} in the given {@code aClass}. If the class
	 * has no acceptable field the class hierarchy is traversed until a field is
	 * found or the root of the class hierarchy is reached without finding a field.
	 * 
	 * @param aClass the class where to look for the field
	 * @param aName  the name of the field
	 * @return the <code>Field</code> instance found or <code>null</code>
	 */
	private Field searchField(Class<?> aClass, String aName) {
		Field l_ret = getField(aClass, aName);

		if (l_ret == null) {
			Class<?> l_sc = getSuperClass(aClass);

			if (l_sc != null) {
				l_ret = searchField(l_sc, aName);
			}
		}
		return l_ret;
	}

	/**
	 * Make the field accessible
	 * 
	 * @param field The field
	 */
	private void setAccessible() {
		if (field != null) {
			field.setAccessible(true);
		}
	}
}
