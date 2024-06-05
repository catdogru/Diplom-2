package ru.yandex.stellar.burgers.model.ingredient;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.util.stream.Collectors.toList;
import static ru.yandex.stellar.burgers.extensions.ArrayExtensions.getRandomSublist;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(NON_NULL)
public class Ingredients {
    List<String> ingredients;

    public static List<String> collectRandomIngredientList(List<Ingredient> availableIngredients) {
        return getRandomSublist(availableIngredients, RandomUtils.nextInt(1, 4))
                .stream()
                .map(Ingredient::getId)
                .collect(toList());
    }
}
