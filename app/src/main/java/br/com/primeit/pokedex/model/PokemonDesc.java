package br.com.primeit.pokedex.model;

import java.util.List;

public class PokemonDesc {

    public List<PokemonDesc.genera> getGenera() {
        return genera;
    }

    public List<flavor_text> getFlavor() {
    return flavor_text_entries;
    }

    public class flavor_text {
        private String flavor_text;
        private Language language;

        @Override
        public String toString() {
            return flavor_text;
        }
    }
    public class Language{
        private String name;

        public String getL() {
            return name;
        }
    }


    private List<flavor_text> flavor_text_entries;

    public class genera {
        private String genus;

        @Override
        public String toString() {
            return genus;
        }
    }

    private List<genera> genera;

    @Override
    public String toString() {
        return "PokemonDesc{" +
                "flavor_text_entries=" + flavor_text_entries.get(0) +
                ", genera=" + genera.get(7) +
                '}';
    }

    public flavor_text getFirstEnglishEntry(){
        for (flavor_text f : flavor_text_entries) {
            if(f.language.getL().equals("en")){
                return f;
            }
        }
    return null;
    }
}
