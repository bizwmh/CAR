/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.util;

import java.lang.reflect.Field;
import java.util.stream.Stream;

/**
 * String Field Initializer.
 * <p>
 * Fields are initialized with the name of the field.<br>
 * If the field name contains underscores:
 * <ul>
 * <li>a single underscore is replaced by a dot '.'
 * <li>a double underscore is replaced by a dash '-'
 * <li>a triple underscore is replaced by an underscore '_'
 * </ul>
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public interface SFI {

	/**
	 * All static String fields are initialized with their field name.<br>
	 * This is done only if the field is of type <code>String</code> and the
	 * value is <code>null</code>.
	 * 
	 * @param aClass the class to initialize
	 */
	static void initialize(Class<?> aClass) {
		setFields(aClass, null);
	}

	/**
	 * All String instance fields of the given object are initialized with their
	 * field name.<br>
	 * This is done only if the field is of type <code>String</code> and the
	 * value is <code>null</code>.
	 * 
	 * @param anObject the object to initialize
	 */
	static void initialize(Object anObject) {
		setFields(anObject.getClass(), anObject);
	};

	private static boolean isNull(Field aField, Object anObject) {
		try {
			return aField.get(anObject) == null;
		} catch (IllegalArgumentException | IllegalAccessException anEx) {
			return false;
		}
	};

	private static boolean isString(Field aField) {
		return aField.getType().equals(String.class);
	}

	private static void setFields(Class<?> aClass, Object anObject) {
		Field[] l_fields = aClass.getDeclaredFields();

		Stream.of(l_fields).filter(f -> isString(f) && isNull(f, anObject))
			.forEach(field -> {
				try {
					String l_name = field.getName();
					l_name = Underscore.apply(l_name);

					field.setAccessible(true);
					field.set(anObject, l_name);
				} catch (IllegalArgumentException
					| IllegalAccessException anEx) {
					throw new RuntimeException(anEx);
				}
			});
	}
}
