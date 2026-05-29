/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import biz.car.SYS;
import biz.car.XRuntimeException;
import biz.car.bundle.MSG;

/**
 * Utility methods for handling field injection.
 */
public class XField implements Consumer<Field> {

	/**
	 * Returns a map of all fields declared directly in the given class, keyed by
	 * field name. Unlike {@link #forClass(Class)}, the class hierarchy is not
	 * traversed — only the fields declared in {@code aClass} itself are included.
	 *
	 * @param aClass the class whose declared fields are to be collected
	 * @return a {@link Map} from field name to {@code XField} instance; never
	 *         {@code null}
	 */
	public static Map<String, XField> fieldMap(Class<?> aClass) {
		Field[] l_fields = aClass.getDeclaredFields();
		Map<String, XField> l_ret = new HashMap<String, XField>();

		for (Field l_field : l_fields) {
			String l_key = l_field.getName();
			XField l_value = new XField(aClass);

			l_value.accept(l_field);
			l_ret.put(l_key, l_value);
		}
		return l_ret;
	}

	/**
	 * Returns a map of all declared fields of the given class and its superclasses,
	 * keyed by field name. The class hierarchy is traversed bottom-up; if the same
	 * field name appears in both a subclass and a superclass, the subclass field
	 * takes precedence.
	 * <p>
	 * Each entry wraps the field in an {@code XField} instance that reflects the
	 * field's accessibility and modifier state. Fields that cannot be accessed are
	 * included in the map but their {@link XField#isUsable()} method will return
	 * {@code false}.
	 *
	 * @param aClass the class whose fields are to be collected; must not be
	 *               {@code null}
	 * @return a {@link Map} from field name to {@code XField} instance; never
	 *         {@code null}
	 * @throws NullPointerException if {@code aClass} is {@code null}
	 */
	public static Map<String, XField> forClass(Class<?> aClass) {
		Class<?> l_class = Objects.requireNonNull(aClass);
		Map<String, XField> l_ret = new HashMap<String, XField>();

		while (l_class != null) {
			Field[] l_fa = l_class.getDeclaredFields();
			String l_name;
			XField l_xf;

			for (Field l_field : l_fa) {
				l_name = l_field.getName();

				if (!l_ret.containsKey(l_name)) {
					l_xf = new XField(l_class);

					l_xf.accept(l_field);
					l_ret.put(l_name, l_xf);
				}
			}
			l_class = l_class.getSuperclass();
		}
		return l_ret;
	}

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
	 * Returns the value of this field for the given object instance.
	 *
	 * @param aObject the object from which to read the field value; may be
	 *                {@code null} for static fields
	 * @return the value of the field, or {@code null} if the field holds
	 *         {@code null}
	 * @throws biz.car.XRuntimeException if the field cannot be read
	 */
	public Object getValue(final Object aObject) {
		try {
			return field.get(aObject);
		} catch (Exception anEx) {
			throw SYS.LOG.exception(anEx);
		}
	};

	/**
	 * Checks if the field in the given object is <code>null</code>.
	 * 
	 * @param anObject the object to check
	 * @return <code>true</code> if this field is <code>null</code>
	 */
	public boolean isNull(Object anObject) {
		try {
			return field.get(anObject) == null;
		} catch (IllegalArgumentException | IllegalAccessException anEx) {
			return false;
		}
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
