/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.wmh.car.util;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Optional;

import biz.wmh.car.SYS;
import biz.wmh.car.XRuntimeException;
import biz.wmh.car.bundle.MSG;

/**
 * <code>Class</code> related functions.
 * 
 * @version 2.0.0 08.01.2026 08:32:08
 */
public interface ClassUtil {

	/**
	 * A registry for <code>Class</code>es.
	 */
	ObjectRegistry<Class<?>> Registry = new ObjectRegistry<Class<?>>();

	/**
	 * Tries to find the URL of a resource with the given name.
	 * 
	 * @param aClass    the class loader of the given class will be used to find the
	 *                  resource
	 * @param aResource the name of the resource
	 * @return the optional URL of the resource.
	 */
	static Optional<URL> getResource(Class<?> aClass, String aResource) {
		ClassLoader l_cl = aClass.getClassLoader();
		URL l_ret = l_cl.getResource(aResource);

		return Optional.ofNullable(l_ret);
	}

	/**
	 * Tries to find the URL of a resource with the given name.<br>
	 * The class loader of this interface will be used to find the resource
	 * 
	 * @param aResource the name of the resource
	 * @return the optional URL of the resource.
	 */
	static Optional<URL> getResource(String aResource) {
		return getResource(ClassUtil.class, aResource);
	}

	/**
	 * Tries to find the URL of a resource with the given name.
	 * 
	 * @param aClass    the class loader of the given class will be used to find the
	 *                  resource
	 * @param aResource the name of the resource
	 * @return the URL of the resource.
	 * @throws XRuntimeException if the resource could not be found
	 */
	static URL getResourceNonNull(Class<?> aClass, String aResource) {
		Optional<URL> l_url = getResource(aClass, aResource);

		return l_url.orElseThrow(() -> SYS.LOG.exception(MSG.RESOURCE_NOT_FOUND, aResource));
	}

	/**
	 * Tries to find the URL of a resource with the given name.<br>
	 * The class loader of this interface will be used to find the resource
	 * 
	 * @param aResource the name of the resource
	 * @return the optional URL of the resource.
	 * @throws XRuntimeException if the resource could not be found
	 */
	static URL getResourceNonNull(String aResource) {
		return getResourceNonNull(ClassUtil.class, aResource);
	}

	/**
	 * Creates a new class instance
	 * 
	 * @param <T>       the type of the created object instance
	 * @param aClass    the class holding the constructor
	 * @param anArgList the constructor arguments
	 * @return the new object instance
	 */
	@SuppressWarnings("unchecked")
	static <T> T newInstance(Class<?> aClass, Object... anArgList) {
		try {
			Constructor<?> l_con = getConstructor(aClass, anArgList);
			T l_ret = (T) l_con.newInstance(anArgList);

			return l_ret;
		} catch (Throwable anEx) {
			throw SYS.LOG.exception(anEx);
		}
	}

	/**
	 * Instance fields will be initialized by the values found in the given
	 * configuration file. This is done for fields where the field name matches the
	 * entry in the configuration file.<br>
	 * If the field name contains the '_' character (not as first) and this field
	 * name is not a key value then the '_' is replaced by '.' and then used again
	 * as a key value.
	 * 
	 * @param aClass  the class to use for the initialization
	 * @param aConfig the configuration to use for the initialization
	 * @param anObj   the class instance whose fields to initialize<br>
	 * 
	 */

	/**
	 * Looks up a class constructor.<br>
	 * 
	 * @param aClass the class where to look up the constructor
	 * @param anArg  the arguments building the signature of the constructor
	 * @return the constructor found
	 * @throws NoSuchMethodException if the constructor was not found
	 * @throws SecurityException     see <code>Class.getConstructor</code>
	 */
	private static Constructor<?> getConstructor(Class<?> aClass,
			Object... anArg) throws NoSuchMethodException, SecurityException {

		Class<?>[] l_args = typeOf(anArg);
		Constructor<?> l_ret = aClass.getConstructor(l_args);

		return l_ret;
	}

	/**
	 * Converts <code>Object</code>s to there corresponding class type.
	 * 
	 * @param anObj the array of objects to convert
	 * @return the array of object types
	 */
	private static Class<?>[] typeOf(Object[] anObj) {
		int l_len = anObj.length;
		Class<?>[] l_ret = new Class[l_len];

		for (int i = 0; i < l_len; i++) {
			l_ret[i] = anObj[i].getClass();
		}
		return l_ret;
	}
}
