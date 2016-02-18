/*
 * Copyright 2012 Richard Beech rp.beech@gmail.com
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.digitale.utils;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActionAdapter extends Action {

protected Actor target;
protected boolean done;

public ActionAdapter() {
done = false;
}

@Override
public void setTarget(Actor target) {
this.target = target;
}

@Override
public Actor getTarget() {
return target;
}

@Override
public boolean act(float delta) {
	return done;

}

public boolean isDone() {
return done;
}

public Action copy() {
throw new UnsupportedOperationException("not implemented");
}

}