/* * Copyright 2012 Richard Beech rp.beech@gmail.com * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language * governing permissions and limitations under the License. */package com.digitale.myepiclife.android;import java.util.ArrayList;public interface WidgetMELEventInterface  {		public int getTemplateUid();	public void setTemplateUid(Integer templateUid);	public String getFrequency() ;	public void setFrequency(String string);	public long getFirstOccurance();	public void setFirstOccurance(long firstOccurance) ;	public ArrayList<CompletionDef> getCompletionList() ;	public void setCompletionList(ArrayList<CompletionDef> completionList);	public long getUid();	public void setUid(long uid) ;	public String getUnitType() ;	public void setUnitType(String unitType);	public String getReminderPeriod() ;	public void setReminderPeriod(String string) ;	public String getEventName();	public void setEventName(String eventName) ;	public String getCatagoryName() ;	public void setCatagoryName(String catagoryName) ;	public String getDescription() ;	public void setDescription(String description);   	}