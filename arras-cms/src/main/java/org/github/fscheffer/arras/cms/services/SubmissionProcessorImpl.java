package org.github.fscheffer.arras.cms.services;

import org.apache.tapestry5.internal.services.RequestPageCache;
import org.apache.tapestry5.internal.structure.ComponentPageElement;
import org.apache.tapestry5.internal.structure.Page;
import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.apache.tapestry5.json.JSONObject;
import org.github.fscheffer.arras.ArrasConstants;

public class SubmissionProcessorImpl implements SubmissionProcessor {

    private RequestPageCache pageCache;

    public SubmissionProcessorImpl(RequestPageCache pageCache) {
        this.pageCache = pageCache;
    }

    @Override
    public void process(JSONObject object) {

        for (String completeId : object.keys()) {

            Object content = object.get(completeId);

            ComponentPageElement component = findComponent(completeId);

            boolean handled = component.triggerEvent(ArrasConstants.UPDATE_CONTENT, new Object[] { content }, null);

            if (!handled) {
                throw new TapestryException(
                                            String.format("Request event '%s' (on component %s) was not handled; you must provide a matching event handler method in the component or in one of its containers.",
                                                          ArrasConstants.UPDATE_CONTENT, component.getCompleteId()),
                                            component, null);
            }
        }

    }

    private ComponentPageElement findComponent(String completeId) {

        int idx = completeId.indexOf(":");

        String componentPageName = completeId.substring(0, idx);
        String nestedId = completeId.substring(idx + 1);

        Page containerPage = this.pageCache.get(componentPageName);

        return containerPage.getComponentElementByNestedId(nestedId);
    }

    private void processContents(ComponentPageElement component, Object content) {

        // if this component is within a loop, it could be used for multiple (different) contexts

        //        for (String rawContext : contentsObj.keys()) {
        //
        //            JSONArray values = contentsObj.getJSONArray(rawContext);
        //
        //            EventContext context = this.contextPathEncoder.decodePath(rawContext);
        //
        //            component.triggerContextEvent(ArrasConstants.PREPARE_FOR_UPDATES, context, null);
        //
        //            for (int i = 0; i < values.length(); i++) {
        //
        //                component.triggerContextEvent(ArrasConstants.BEFORE_UPDATE_CONTENT, context, null);
        //
        //                component.triggerContextEvent(ArrasConstants.AFTER_UPDATE_CONTENT, context, null);
        //            }
        //
        //            component.triggerContextEvent(ArrasConstants.CLEANUP_AFTER_UPDATES, context, null);
        //        }
    }
}
