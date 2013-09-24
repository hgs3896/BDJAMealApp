package com.bdjamealapp.data;

public class Food {

    private FoodType type;
    public String food;
    public int foodRate;

    public enum FoodType {
        Rice, Soup, Pasta, Dessert, Bread, Fish, Corn, Kim, Kimchi, Source, Pork, Salad, Chicken, Beans, Fruit, Etc
    }

    ;

    public void setRate(int rate) {
        foodRate = rate;
    }

    @Override
    public String toString() {
        return food;
    }

    public Food() {
        this("음식 없음");
    }

    public Food(String food) {
        this.food = food.trim();
        setFoodType();
    }

    public FoodType getFoodType() {
        return type;
    }

    public String getFoodTypeName() {
        switch (type) {
            case Rice:
                return "밥류";
            case Soup:
                return "국류";
            case Pasta:
                return "면류";
            case Dessert:
                return "디저트";
            case Bread:
                return "빵";
            case Fish:
                return "생선요리";
            case Kimchi:
                return "김치";
            case Kim:
                return "김";
            case Source:
                return "소스";
            case Pork:
                return "돼지고기";
            case Salad:
                return "야채";
            case Chicken:
                return "닭요리";
            case Beans:
                return "콩요리";
            case Fruit:
                return "과일";
            case Corn:
                return "옥수수";
            case Etc:
            default:
                return "기타";
        }
    }

    private void setFoodType() {
        if (food.endsWith("밥"))
            type = FoodType.Rice;

        else if (food.endsWith("국") || food.endsWith("탕")
                || food.endsWith("스프") || food.endsWith("찌개")
                || food.endsWith("육개장"))
            type = FoodType.Soup;

        else if (food.contains("닭") || food.startsWith("치킨")
                || food.endsWith("깐풍기"))
            type = FoodType.Chicken;

        else if (food.contains("삼치") || food.startsWith("임연수어")
                || food.contains("코다리"))
            type = FoodType.Fish;

        else if (food.startsWith("돼지") || food.contains("돈까스")
                || food.startsWith("돈육") || food.startsWith("제육"))
            type = FoodType.Pork;

        else if (food.endsWith("면") || food.endsWith("잡채")
                || food.endsWith("스파게티") || food.contains("국수"))
            type = FoodType.Pasta;

        else if (food.contains("야쿠르트") || food.contains("요구르트")
                || food.endsWith("또띠아") || food.contains("요거트")
                || food.contains("요플레") || food.contains("핫도그")
                || food.contains("케익") || food.contains("케잌")
                || food.contains("요플레") || food.contains("웨지감자")
                || food.contains("꽈배기"))
            type = FoodType.Dessert;

        else if (food.endsWith("김치") || food.contains("동치미")
                || food.contains("깍두기") || food.contains("배추겉절이"))
            type = FoodType.Kimchi;

        else if (food.endsWith("무침") || food.endsWith("나물")
                || food.endsWith("샐러드") || food.endsWith("생채")
                || food.endsWith("겉절이") || food.endsWith("색채")
                || food.endsWith("채볶음") || food.contains("시금치")
                || food.contains("야채") || food.contains("탕평채"))
            type = FoodType.Salad;

        else if (food.contains("옥수수") || food.contains("콘"))
            type = FoodType.Corn;

        else if (food.contains("두부") || food.contains("콩")
                || food.contains("두유"))
            type = FoodType.Beans;

        else if (food.endsWith("빵") || food.endsWith("토스트"))
            type = FoodType.Bread;

        else if (food.endsWith("사과") || food.endsWith("바나나")
                || food.endsWith("감") || food.endsWith("귤")
                || food.endsWith("오렌지") || food.endsWith("수박"))
            type = FoodType.Fruit;

        else if (food.contains("고추장") || food.contains("소스")
                || food.contains("드레싱"))
            type = FoodType.Source;

        else if (food.contains("김"))
            type = FoodType.Kim;

        else
            type = FoodType.Etc;
    }
}
