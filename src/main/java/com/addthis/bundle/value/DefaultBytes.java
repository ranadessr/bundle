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

import com.addthis.basis.util.LessBytes;

import com.fasterxml.jackson.annotation.JsonCreator;

public class DefaultBytes implements ValueBytes {

    private final byte[] value;

    @JsonCreator protected DefaultBytes(byte[] value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value != null ? "byte[" + value.length + "]" : "null";
    }

    @Override
    public TYPE getObjectType() {
        return TYPE.BYTES;
    }

    @Override
    public byte[] asNative() {
        return value;
    }

    @Override
    public ValueArray asArray() {
        ValueArray arr = ValueFactory.createArray(1);
        arr.add(this);
        return arr;
    }

    @Override
    public Numeric asNumeric() {
        return asLong();
    }

    @Override
    public ValueLong asLong() {
        try {
            return ValueFactory.create(LessBytes.toLong(value));
        } catch (Exception ex) {
            throw new ValueTranslationException(ex);
        }
    }

    @Override
    public ValueDouble asDouble() {
        try {
            return ValueFactory.create(Double.longBitsToDouble(LessBytes.toLong(value)));
        } catch (Exception ex) {
            throw new ValueTranslationException(ex);
        }
    }

    @Override
    public ValueBytes asBytes() throws ValueTranslationException {
        return this;
    }

    @Override
    public int size() {
        return value.length;
    }
}
