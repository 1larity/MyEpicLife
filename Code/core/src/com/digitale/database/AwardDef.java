/*
 * Copyright 2012 Richard Beech rp.beech@gmail.com
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * Avatardef Class, stores Avatar graphic information, mainly used in char creator
 */
package com.digitale.database;
/** asset definition container**/
public class AwardDef {

	private int uid;
	private String quality;
	private String title;
	private String text;
	private String rule;
	private int pointValue;

	// private blob data;

	public AwardDef(int uid, String quality, String title,
			String text, String rule,int pointValue) {
		this.setUid(uid);
		this.setQuality(quality);
		this.setTitle(title);
		this.setText(text);
		this.setRule(rule);
		this.setPointValue(pointValue);
	}

	public AwardDef() {
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

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public int getPointValue() {
		return pointValue;
	}

	public void setPointValue(int pointValue) {
		this.pointValue = pointValue;
	}


}