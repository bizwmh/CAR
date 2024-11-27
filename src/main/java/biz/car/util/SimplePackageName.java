/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.util;

/**
 * Provides the simple name of a package.
 * 
 * @version 1.0.0 23.10.2024 11:57:56
 */
public interface SimplePackageName {

	static String get(Class<?> aClass) {
		String l_pn = aClass.getPackageName();
		int l_dot = l_pn.lastIndexOf('.');
		String l_ret = l_pn.substring(l_dot + 1);

		return l_ret;
	}
}
