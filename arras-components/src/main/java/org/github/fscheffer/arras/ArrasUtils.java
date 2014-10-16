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

    public static final String buildUrl(String host, String port, String context) {

        StringBuilder builder = new StringBuilder();

        builder.append(host);

        if (InternalUtils.isNonBlank(port)) {
            builder.append(":");
            builder.append(port);
        }

        if (InternalUtils.isNonBlank(context)) {

            if (!context.startsWith("/")) {
                builder.append("/");
            }

            builder.append(context);
        }

        return builder.toString();
    }

    public static final String appendPath(String baseUrl, String path) {
        return path.startsWith("/") ? baseUrl + path : baseUrl + "/" + path;
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

}
