package com.ai.xstack.kolesnyk.service.impl;

import com.ai.xstack.kolesnyk.dto.DishDto;
import com.ai.xstack.kolesnyk.dto.CreateDietDto;
import com.ai.xstack.kolesnyk.dto.MealDto;
import com.ai.xstack.kolesnyk.entity.DishEntity;
import com.ai.xstack.kolesnyk.repository.DishRepository;
import com.ai.xstack.kolesnyk.service.DietService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DietServiceImpl implements DietService {

    private static final double KCAL_PER_GRAM_FOR_PROTEINS = 4d;
    private static final double KCAL_PER_GRAM_FOR_FATS = 9d;
    private static final double KCAL_PER_GRAM_FOR_CARBOHYDRATES = 4d;

    private static final List<DishDto> MENU = new ArrayList<>();

    private final DishRepository dishRepository;

    @Override
    public List<MealDto> generateDayRation(CreateDietDto createDietDto) {
        selectRandomDishesForMenu(MENU);

        int kcal = createDietDto.getKcal();
        int numberOfMeals = createDietDto.getNumberOfMeals();
        double proteinsCoefficient = createDietDto.getProteinsCoefficient();
        double fatsCoefficient = createDietDto.getFatsCoefficient();
        double carbohydratesCoefficient = createDietDto.getCarbohydratesCoefficient();
        int kcalPerMeal = kcal / numberOfMeals;

        ArrayList<MealDto> meals = new ArrayList<>();
        for (int i = 0; i < numberOfMeals; i++) {
            meals.add(MealDto.builder().kcal(kcalPerMeal).build()); // breakfast
        }

        meals.forEach(meal -> setDishesForMeal(meal, proteinsCoefficient, fatsCoefficient, carbohydratesCoefficient));
        return meals;
    }

    private void selectRandomDishesForMenu(List<DishDto> menu) {
        menu.clear();
        List<DishEntity> dishes = dishRepository.findAll();
        for (int i = 0; i < 4; i++) {
            int randomNumber = new Random().nextInt(dishes.size());
            DishEntity randomDish = dishes.get(randomNumber);
            menu.add(new DishDto(randomDish.getMassInGrams(), randomDish. getDesc(), randomDish.getProteins(), randomDish.getFats(), randomDish.getCarbohydrates()));
        }
    }

    /**
     * using genetic algorithm.
     * create bestCombination.
     * set meal.setDishes(bestCombination) -> List<DishDto>
     *
     * @param mealDto
     * @param proteinsCoefficient
     * @param fatsCoefficient
     * @param carbohydratesCoefficient
     */
    private static void setDishesForMeal(MealDto mealDto, double proteinsCoefficient, double fatsCoefficient, double carbohydratesCoefficient) {
        int totalKcal = mealDto.getKcal(); // 500kcal
        double gramProteins = totalKcal * proteinsCoefficient / KCAL_PER_GRAM_FOR_PROTEINS; //200kcal = 50g
        double gramFats = totalKcal * fatsCoefficient / KCAL_PER_GRAM_FOR_FATS; //150kcal = 37.5g
        double gramCarbohydrates = totalKcal * carbohydratesCoefficient / KCAL_PER_GRAM_FOR_CARBOHYDRATES; //150kcal = 16.7g

        List<DishDto> bestCombination = findBestCombination(totalKcal, gramProteins, gramFats, gramCarbohydrates);
        List<DishDto> groupedDishesForMeal = groupDishesByDescriptionWithTotalMass(bestCombination);
        mealDto.setDishes(groupedDishesForMeal);
    }

    private static List<DishDto> groupDishesByDescriptionWithTotalMass(List<DishDto> bestCombination) {
        Map<String, List<DishDto>> groupedByDescription = bestCombination.stream()
                .collect(Collectors.groupingBy(DishDto::getDesc));

        List<DishDto> dishesList = new ArrayList<>();
        for (Map.Entry<String, List<DishDto>> entry : groupedByDescription.entrySet()) {
            String desc = entry.getKey();
            List<DishDto> dishes = entry.getValue();
            int totalMass = dishes.stream().mapToInt(DishDto::getMassInGrams).sum();
            double totalProteins = calculateTotalProteins(dishes);
            double totalFats = calculateTotalFats(dishes);
            double totalCarbohydrates = calculateTotalCarbohydrates(dishes);
            dishesList.add(new DishDto(totalMass, desc, totalProteins, totalFats, totalCarbohydrates));
        }

        return dishesList;
    }

    private static List<DishDto> findBestCombination(int totalKcal, double gramProteins, double gramFats, double gramCarbohydrates) {
        List<DishDto> bestCombination = new ArrayList<>();
        double bestDifference = Double.MAX_VALUE;

        for (int i = 0; i < 1000; i++) {
            List<DishDto> currentCombination = generateRandomCombination(totalKcal);
            double currentProteins = calculateTotalProteins(currentCombination);
            double currentFats = calculateTotalFats(currentCombination);
            double currentCarbohydrates = calculateTotalCarbohydrates(currentCombination);

            double difference = Math.abs(gramProteins - currentProteins) +
                    Math.abs(gramFats - currentFats) +
                    Math.abs(gramCarbohydrates - currentCarbohydrates);

            if (difference < bestDifference) {
                bestDifference = difference;
                bestCombination = currentCombination;
            }
        }

        return bestCombination;
    }

    private static List<DishDto> generateRandomCombination(int totalKcal) {
        List<DishDto> combination = new ArrayList<>();
        int remainingKcal = totalKcal;

        Random random = new Random();

        while (remainingKcal > MENU.stream().mapToDouble(DishDto::getCalories).min().orElse(0)) {
            DishDto randomDish = MENU.get(random.nextInt(MENU.size()));
            combination.add(randomDish);
            remainingKcal -= randomDish.getCalories();
        }

        return combination;
    }

    private static double calculateTotalProteins(List<DishDto> dishes) {
        return dishes.stream().mapToDouble(DishDto::getProteins).sum();
    }

    private static double calculateTotalFats(List<DishDto> dishes) {
        return dishes.stream().mapToDouble(DishDto::getFats).sum();
    }

    private static double calculateTotalCarbohydrates(List<DishDto> dishes) {
        return dishes.stream().mapToDouble(DishDto::getCarbohydrates).sum();
    }

}
