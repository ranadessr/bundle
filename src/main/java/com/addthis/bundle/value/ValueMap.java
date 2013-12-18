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
package com.addthis.bundle.value;

import java.util.Map;
import java.util.Set;

public interface ValueMap extends ValueObject, Iterable<ValueMapEntry> {

    public ValueObject get(String key);

    public ValueObject remove(String key);

    public ValueObject put(String key, ValueObject val);

    public int size();

    public Set<Map.Entry<String, ValueObject>> entrySet();
}