/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.addthis.bundle.core;

import java.util.List;
import java.util.Map;

import com.addthis.bundle.core.list.ListBundle;
import com.addthis.bundle.value.ValueArray;
import com.addthis.bundle.value.ValueFactory;
import com.addthis.bundle.value.ValueMap;
import com.addthis.maljson.JSONObject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class TestBundles {

    private static final Logger log = LoggerFactory.getLogger(TestBundles.class);

    Bundle a;
    Bundle b;

    @Before
    public void setup() {
        a = TestBundle.testBundle(new ListBundle(), new String[][]{
                new String[]{"abc", "def"},
                new String[]{"ghi", "jkl"},
                new String[]{"mno", "pqr"},
        }, false);
        b = TestBundle.testBundle(new ListBundle(), new String[][]{
                new String[]{"zzz", "aaa"},
                new String[]{"yyy", "jkl"},
                new String[]{"abc", "pqr"},
        }, false);
    }

    @Test
    public void testJsonToString() throws Exception {
        List<String> list = Lists.newArrayList("zero", "one", "two");
        ValueArray array = ValueFactory.createValueArray(list);
        assertEquals(list.toString(), array.toString());

        Bundle bundle = new ListBundle();
        bundle.setValue(bundle.getFormat().getField("arrayField"), array);
        Map<String, Object> bundleMap = Maps.newHashMap();
        bundleMap.put("arrayField", list);
        assertEquals(JSONObject.wrap(bundleMap).toString(), Bundles.toJsonString(bundle));

        ValueMap valueMap = ValueFactory.createMap();
        valueMap.put("arrayKey", array);
        bundle.setValue(bundle.getFormat().getField("mapField"), valueMap);
        Map<String, Object> nestedMap = Maps.newHashMap();
        nestedMap.put("arrayKey", list);
        bundleMap.put("mapField", nestedMap);
        assertEquals(JSONObject.wrap(bundleMap).toString(), Bundles.toJsonString(bundle));
    }

    @Test
    public void testaddAllReplace() {
        Bundle c = Bundles.addAll(a, b, true);
        assertEquals(b, c);
        assertEquals(b.getValue(b.getFormat().getField("abc")), "def");
        assertEquals(b.getValue(b.getFormat().getField("ghi")), "jkl");
        assertEquals(b.getValue(b.getFormat().getField("mno")), "pqr");
        assertEquals(b.getValue(b.getFormat().getField("zzz")), "aaa");
        assertEquals(b.getValue(b.getFormat().getField("yyy")), "jkl");
    }
    
    @Test
    public void testaddAllNoReplace() {
        Bundle c = Bundles.addAll(a, b, false);
        assertEquals(b, c);
        assertEquals(b.getValue(b.getFormat().getField("abc")), "pqr");
        assertEquals(b.getValue(b.getFormat().getField("ghi")), "jkl");
        assertEquals(b.getValue(b.getFormat().getField("mno")), "pqr");
        assertEquals(b.getValue(b.getFormat().getField("zzz")), "aaa");
        assertEquals(b.getValue(b.getFormat().getField("yyy")), "jkl");
    }
    
    @Test
    public void testaddAllReplaceString() {
        Bundle c = Bundles.addAll(a, b, "_new");
        assertEquals(b.getValue(b.getFormat().getField("abc")), "pqr");
        assertEquals(b.getValue(b.getFormat().getField("abc_new")), "def");
        assertEquals(b.getValue(b.getFormat().getField("ghi")), "jkl");
        assertEquals(b.getValue(b.getFormat().getField("mno")), "pqr");
        assertEquals(b.getValue(b.getFormat().getField("zzz")), "aaa");
        assertEquals(b.getValue(b.getFormat().getField("yyy")), "jkl");
    }
    
    @Test
    public void testEquals() {
        Bundle c = TestBundle.testBundle(new ListBundle(), new String[][]{
            new String[]{"zzz", "aaa"},
            new String[]{"yyy", "jkl"},
            new String[]{"abc", "pqr"},
        }, false);
        assertTrue(Bundles.equals(b,c));
        Bundle d = TestBundle.testBundle(new ListBundle(), new String[][]{
            new String[]{"zzz", "aab"},
            new String[]{"yyy", "jkl"},
            new String[]{"abc", "pqr"},
        }, false);
        assertFalse(Bundles.equals(b,d));
        Bundle e = TestBundle.testBundle(new ListBundle(), new String[][]{
            new String[]{"zza", "aaa"},
            new String[]{"yyy", "jkl"},
            new String[]{"abc", "pqr"},
        }, false);
        assertFalse(Bundles.equals(b,e));
    }
}
