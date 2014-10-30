// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.github.fscheffer.arras;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.json.JSONObject;

public class ArrasUtils {

    /**
     * Merge source into dest. Entries in source will be added to dest and will override existing entries in dest.
     *
     * @param dest
     * @param source
     */
    public static final void merge(JSONObject dest, JSONObject source) {

        if (source == null) {
            return;
        }

        for (String key : source.keys()) {
            dest.put(key, source.get(key));
        }
    }

    public static final String getPresentableComponentName(Messages messages, ComponentResources resources) {
        return getPresentableComponentName(messages, resources.getId());
    }

    public static final String getPresentableComponentName(Messages messages, String componentId) {

        String key = componentId + "-name";

        if (messages.contains(key)) {
            return messages.get(key);
        }

        return TapestryInternalUtils.toUserPresentable(componentId);
    }

    public static final void addOption(MarkupWriter writer, String option, String value) {

        if (InternalUtils.isNonBlank(value)) {
            addDataAttribute(writer, option, value);
        }
    }

    public static final void addOption(MarkupWriter writer, String option, boolean value) {

        if (value) {
            addDataAttribute(writer, option, value);
        }
    }

    public static final void addDataAttribute(MarkupWriter writer, String option, Object value) {
        writer.attributes("data-" + option, value);
    }

    public static String get(JSONObject data, String property, String defaultValue) {

        String value = String.class.cast(data.opt(property));

        return value != null ? value : defaultValue;
    }
}
