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

package com.github.fscheffer.arras.cms.services;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Flow;
import org.apache.tapestry5.internal.InternalComponentResources;
import org.apache.tapestry5.internal.services.ComponentClassCache;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.plastic.ComputedValue;
import org.apache.tapestry5.plastic.FieldConduit;
import org.apache.tapestry5.plastic.InstanceContext;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.plastic.PlasticField;
import org.apache.tapestry5.services.BindingSource;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.apache.tapestry5.services.transform.TransformationSupport;

import com.github.fscheffer.arras.cms.Content;

public class ContentWorker implements ComponentClassTransformWorker2 {

    private final BindingSource bindingSource;

    private final TypeCoercer   typeCoercer;

    private ComponentClassCache classCache;

    public ContentWorker(BindingSource bindingSource, TypeCoercer typeCoercer, ComponentClassCache classCache) {
        this.bindingSource = bindingSource;
        this.typeCoercer = typeCoercer;
        this.classCache = classCache;
    }

    @Override
    public void transform(PlasticClass plasticClass, TransformationSupport support, MutableComponentModel model) {

        Flow<PlasticField> parametersFields = F.flow(plasticClass.getFieldsWithAnnotation(Content.class));

        for (PlasticField field : parametersFields) {
            convertField(plasticClass, model, field);
        }

    }

    private void convertField(PlasticClass plasticClass, MutableComponentModel model, PlasticField field) {

        Content annotation = field.getAnnotation(Content.class);

        field.claim(annotation);

        String fieldName = field.getName();
        Class<?> fieldType = this.classCache.forName(field.getTypeName());

        String contentId = annotation.contentId();

        if (InternalUtils.isBlank(contentId)) {
            contentId = fieldName;
        }

        ComputedValue<FieldConduit<Object>> computedConduit = createComputedConduit(fieldName, fieldType, contentId,
                                                                                    annotation);

        field.setComputedConduit(computedConduit);
    }

    private ComputedValue<FieldConduit<Object>> createComputedConduit(final String fieldName, final Class<?> fieldType,
                                                                      final String contentId, final Content annotation) {

        return new ComputedValue<FieldConduit<Object>>() {

            @Override
            public FieldConduit<Object> get(InstanceContext context) {

                final InternalComponentResources icr = context.get(InternalComponentResources.class);

                return new FieldConduit<Object>() {

                    private Binding dataBinding;

                    private Binding defaultBinding;

                    {
                        icr.getPageLifecycleCallbackHub().addPageLoadedCallback(new Runnable() {

                            @Override
                            public void run() {
                                dataBinding = createBinding("data " + fieldName, annotation.data());
                                defaultBinding = createDefaultValueBinding();
                            }
                        });
                    }

                    @Override
                    public Object get(Object instance, InstanceContext context) {

                        JSONObject json = JSONObject.class.cast(this.dataBinding.get());

                        Object value = json.opt(contentId);

                        Object object = value == null ? this.defaultBinding.get() : value;

                        return ContentWorker.this.typeCoercer.coerce(object, fieldType);
                    }

                    @Override
                    public void set(Object instance, InstanceContext context, Object newValue) {

                        JSONObject json = JSONObject.class.cast(this.dataBinding.get());

                        json.put(contentId, newValue);
                    }

                    private Binding createDefaultValueBinding() {

                        String expression = annotation.value();

                        return createBinding("default " + fieldName, expression);
                    }

                    private Binding createBinding(String description, String expression) {

                        return ContentWorker.this.bindingSource.newBinding(description, icr, BindingConstants.PROP,
                                                                           expression);
                    }
                };
            }

        };
    }
}
