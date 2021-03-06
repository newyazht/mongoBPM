/**
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.persistence.mongodb.task.model;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;

import org.kie.api.task.model.I18NText;
import org.kie.internal.task.api.model.InternalI18NText;

public class MongoI18NTextImpl implements InternalI18NText {
    
    private Long   id = 0L;

    private String language;

    private String shortText;

    private String text;

    public MongoI18NTextImpl() {}
    
    public MongoI18NTextImpl(I18NText i18NText) {
    	this.id = i18NText.getId();
    	this.language = i18NText.getLanguage();
    	setText(i18NText.getText());
    }
    
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong( id );
        if( language == null ) { 
            language = "";
        }
        out.writeUTF( language );
        
        if( shortText == null ) {
            shortText = "";
        }
        out.writeUTF( shortText );
        
        if( text == null ) { 
            text = "";
        }
        out.writeUTF( text );        
    }
    
    public void readExternal(ObjectInput in) throws IOException,
                                            ClassNotFoundException {
        id = in.readLong();
        language = in.readUTF();
        shortText = in.readUTF();
        text = in.readUTF();        
    }

    public MongoI18NTextImpl(String language,
                    String text) {
        this.language = language;
        if (text != null && text.length() > 256) {
            this.shortText = text.substring(0, 256);
        } else {
            this.shortText = text;
        }
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text != null && text.length() > 256) {
            this.shortText = text.substring(0, 256);
        } else {
            this.shortText = text;
        }
        this.text = text;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((language == null) ? 0 : language.hashCode());
        result = prime * result + ((shortText == null) ? 0 : shortText.hashCode());
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) return true;
        if ( obj == null ) return false;
        if ( !(obj instanceof MongoI18NTextImpl) ) return false;
        MongoI18NTextImpl other = (MongoI18NTextImpl) obj;
        if ( language == null ) {
            if ( other.language != null ) return false;
        } else if ( !language.equals( other.language ) ) return false;
        if ( shortText == null ) {
            if ( other.shortText != null ) return false;
        } else if ( !shortText.equals( other.shortText ) ) return false;
        if ( text == null ) {
            if ( other.text != null ) return false;
        } else if ( !text.equals( other.text ) ) return false;
        return true;
    }
    
    public static String getLocalText(List<MongoI18NTextImpl> list, String prefferedLanguage, String defaultLanguage) {
        for ( MongoI18NTextImpl text : list) {
            if ( text.getLanguage().equals( prefferedLanguage )) {
                return text.getText();
            }
        }
        if (  defaultLanguage == null ) {
            for ( MongoI18NTextImpl text : list) {
                if ( text.getLanguage().equals( defaultLanguage )) {
                    return text.getText();
                }
            }    
        }
        return "";
    }


    
}
