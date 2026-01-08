/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.wmh.car.util;

/**
 * Provides the simple name of a package.
 * 
 * @version 2.0.0 08.01.2026 08:32:08
 */
public interface SimplePackageName {

	static String get(Class<?> aClass) {
		String l_pn = aClass.getPackageName();
		int l_dot = l_pn.lastIndexOf('.');
		String l_ret = l_pn.substring(l_dot + 1);

		return l_ret;
	}
}
