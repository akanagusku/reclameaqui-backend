package br.com.santander.predictbacen.reclameaqui.controller;

import br.com.santander.predictbacen.reclameaqui.model.ComplainPrediction;
import br.com.santander.predictbacen.reclameaqui.model.PredictionModel;
import br.com.santander.predictbacen.reclameaqui.viewmodel.ComplainViewModel;
import br.com.santander.predictbacen.reclameaqui.viewmodel.PredictionModelViewModel;
import br.com.santander.predictbacen.reclameaqui.viewmodel.QuantityPredictionModelCompetition;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class PredictionModelController {
    @RequestMapping("/prediction-models")
    public ArrayList<PredictionModelViewModel> getPredictionModels() {
        return new PredictionModel().findAllActive();
    }

    @RequestMapping("/prediction-models/calculate-hit-percentage")
    public void calculateHitPercentage() {
        PredictionModel predictionModel = new PredictionModel();
        ComplainPrediction complainPrediction = new ComplainPrediction();

        ArrayList<ComplainPrediction> openedPredictions = complainPrediction.getOpenedPredictedComplain();

        for (ComplainPrediction openedPrediction : openedPredictions) {
            openedPrediction.complain.score = (openedPrediction.complain.score == 0) ? 0.1 : openedPrediction.complain.score;
            openedPrediction.hitPercentage = Math.abs(Math.abs((openedPrediction.predictedScore - openedPrediction.complain.score)) - 10) * 10;
        }

        complainPrediction.updatedFailPercentage(openedPredictions);
        predictionModel.updateHitPercentage();
    }

    @RequestMapping("/prediction-models/{id}/predictions")
    public ArrayList<ComplainViewModel> getPredictionsByModel(@PathVariable(value="id") Integer id) {
        PredictionModel predictionModel = new PredictionModel();

        return predictionModel.getPredictionsByModelId(id);
    }

    @RequestMapping("/prediction-models/champion/hit-percentage")
    public Double getChampionHitPercentage() {
        PredictionModel predictionModel = new PredictionModel();

        return predictionModel.getChampion().hitPercentage;
    }

    @RequestMapping("/prediction-models/competition-results")
    public ArrayList<QuantityPredictionModelCompetition> getModelCompetition() {
        PredictionModel predictionModel = new PredictionModel();

        return predictionModel.getModelCompetition();
    }
}
