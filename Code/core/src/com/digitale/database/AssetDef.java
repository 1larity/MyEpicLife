/*
 * Copyright 2012 Richard Beech rp.beech@gmail.com
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * Avatardef Class, stores Avatar graphic information, mainly used in char creator
 */
package com.digitale.database;
/** asset definition container**/
public class AssetDef {

	private int uid;
	private String assetName;
	private String filename;
	private String extension;
	private String type;

	// private blob data;

	public AssetDef(int uid, String assetName, String imagename,
			String extension, String type) {
		this.setUid(uid);
		this.setAssetName(assetName);
		this.setExt(extension);
		this.setType(type);
	}

	public AssetDef() {
		// TODO Auto-generated constructor stub
	}

	/** get asset unique ID **/
	public int getUid() {
		return uid;
	}

	/** set asset unique ID **/
	public void setUid(int uid) {
		this.uid = uid;
	}

	/** get asset friendly name **/
	public String getAssetName() {
		return assetName;
	}

	/** set asset friendly name **/
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	/** get asset file extension **/
	public String getExt() {
		return extension;
	}

	/** set asset file extension **/
	public void setExt(String ext) {
		this.extension = ext;
	}

	/** get asset type (sound/font/music/icon/texture/ui) **/
	public String getType() {
		return type;
	}

	/** set asset type (sound/font/music/icon/texture/ui) **/
	public void setType(String type) {
		this.type = type;
	}

	/** get asset filename without extension **/
	public String getFilename() {
		return filename;
	}

	/** set asset filename without extension **/
	public void setFilename(String filename) {
		this.filename = filename;
	}
}