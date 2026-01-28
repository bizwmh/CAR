/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.io;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.List;

/**
 * Interface for listening to directory change events.
 */
public interface DirectoryListener {

	/**
	 * Called when a file system event occurs in the registered directory.
	 * 
	 * @param aPath   the path of the directory where the event occurred
	 * @param aEvents the list of watch events containing details about the change
	 */
	void onEvent(Path path, List<WatchEvent<?>> aEvents);
}
