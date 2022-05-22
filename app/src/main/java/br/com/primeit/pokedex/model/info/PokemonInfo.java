package br.com.primeit.pokedex.model.info;

import java.util.List;
import java.util.Locale;

public class PokemonInfo {
    private int height;
    private List<Ability> abilities;
    private int id;
    private int weight;
    private List<Types> types;
    private String name;

    public PokemonInfo(String name) {
        this.name = name;
    }

    public String getHeight() {
        return ""+height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeight() {
        return ""+weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<Types> getTypes() {
        return types;
    }



    public void setTypes(List<Types> types) {
        this.types = types;
    }

    public String getName() {
        return name.substring(0,1).toUpperCase(Locale.ROOT) + name.substring(1);
    }

    public void setName(String name) {
        this.name = name;
    }

    public class Types {
        private int slot;
        private Type type;


        @Override
        public String toString() {
            return "Types{" +
                    "slot=" + slot +
                    ", type=" + type +
                    '}';
        }

        public String getName() {
            return type.toString();
        }



        private class Type {
            private String name;
            private String url;

            @Override
            public String toString() {
                return name;
            }

        }
    }

    private class Ability {
        private int slot;
        private boolean is_hidden;

        private AbilityInfo ability;



        private class AbilityInfo {
            private String name;
            private String url;

            @Override
            public String toString() {

                String nameUpCase = name.substring(0,1).toUpperCase(Locale.ROOT) + name.substring(1);
                String nameSpaced = nameUpCase.replace('-', ' ');
                return nameSpaced;
            }
        }

        @Override
        public String toString() {
            return ""+ability.toString();
        }
    }

    @Override
    public String toString() {
        return "PokemonInfo{" +
                "height=" + height +
                ", abilities=" + abilities +
                ", id=" + id +
                ", weight=" + weight +
                ", types=" + types +
                ", name='" + name + '\'' +
                '}';
    }
}
