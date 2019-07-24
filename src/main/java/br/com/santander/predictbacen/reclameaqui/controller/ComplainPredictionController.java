package br.com.santander.predictbacen.reclameaqui.controller;

import br.com.santander.predictbacen.reclameaqui.model.Complain;
import br.com.santander.predictbacen.reclameaqui.model.ComplainPrediction;
import br.com.santander.predictbacen.reclameaqui.model.PredictionModel;
import hex.genmodel.MojoModel;
import hex.genmodel.easy.EasyPredictModelWrapper;
import hex.genmodel.easy.RowData;
import hex.genmodel.easy.prediction.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@RestController
public class ComplainPredictionController {

    @RequestMapping("/complain-prediction/predict-batch")
    public void predictComplains() {
        EasyPredictModelWrapper.Config config = new EasyPredictModelWrapper.Config();
        config.setConvertUnknownCategoricalLevelsToNa(true);

        String folderPath = "/Users/t722286/Downloads/models";
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();
        String modelCategory;

        Complain complain = new Complain();
        ArrayList<Complain> openComplains = complain.getOpenComplainWithoutPrediction();

        for (File currentFile: listOfFiles) {

            for (Complain currentComplain: openComplains) {
                try {
                    config.setModel(MojoModel.load(folderPath + "/" + currentFile.getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                EasyPredictModelWrapper model = new EasyPredictModelWrapper(config);

                PredictionModel predictionModel = new PredictionModel();
                modelCategory = config.getModel().getModelCategory().name();
                predictionModel.saveToDatabase(currentFile.getName(), modelCategory, folderPath);

                RowData row = new RowData();
                row.put("additionalFields", currentComplain.additionalFields);
                row.put("moderationUserName", currentComplain.moderationUserName);
                row.put("otherProblemType", currentComplain.otherProblemType);
                row.put("solved", currentComplain.solved);
                row.put("dealAgain", Double.valueOf(currentComplain.dealAgain));
                row.put("otherProductType", currentComplain.otherProductType);
                row.put("type", currentComplain.type);
                row.put("evaluation", currentComplain.evaluation);
//                row.put("score", currentComplain.score);
                row.put("userState", currentComplain.userState);
                row.put("userRequestedDelete", Double.valueOf(currentComplain.userRequestedDelete));
                row.put("requesterName", currentComplain.requesterName);
                row.put("additionalInfo", currentComplain.additionalInfo);
                row.put("modified", Double.valueOf(currentComplain.modified.getMillis()));
                row.put("id", currentComplain.id);
                row.put("evaluated", Double.valueOf(currentComplain.evaluated));
                row.put("publishedEmailSent", Double.valueOf(currentComplain.publishedEmailSent));
                row.put("deletionReason", currentComplain.deletionReason);
                row.put("read", Double.valueOf(currentComplain.read));
                row.put("marketplaceComplain", currentComplain.marketplaceComplain);
                row.put("created", Double.valueOf(currentComplain.created.getMillis()));
                row.put("ip", currentComplain.ip);
                row.put("moderationReasonDescription", currentComplain.moderationReasonDescription);
                row.put("frozen", Double.valueOf(currentComplain.frozen));
                row.put("requestEvaluation", currentComplain.requestEvaluation);
                row.put("moderateReason", currentComplain.moderateReason);
                row.put("problemType", "0000000000000000");
                row.put("deletedIp", currentComplain.deletedIp);
                row.put("status", currentComplain.status);
                row.put("userCity", currentComplain.userCity);
                row.put("description", currentComplain.description);
                row.put("title", currentComplain.title);
                row.put("complainOrigin", currentComplain.complainOrigin);
                row.put("firstInteractionDate", Double.valueOf(currentComplain.firstInteractionDate.getMillis()));
                row.put("hasReply", Double.valueOf(currentComplain.hasReply));
                row.put("compliment", Double.valueOf(currentComplain.compliment));
                row.put("legacyId", currentComplain.legacyId);
                row.put("company", 98);

                try {
                    switch (modelCategory) {
                        case "Regression":
                            RegressionModelPrediction regressionModelPrediction = model.predictRegression(row);
                            ComplainPrediction complainPrediction = new ComplainPrediction();
                            complainPrediction.saveToDatabase(regressionModelPrediction.value, currentFile.getName(), currentComplain.id);
                            break;
                        case "Multinomial":
                            MultinomialModelPrediction multinomialModelPrediction = model.predictMultinomial(row);
                            System.out.println(currentFile.getName() + " " + config.getModel().getModelCategory().name() + " valor -> " + multinomialModelPrediction.label);
                            break;
                        case "Clustering":
                            ClusteringModelPrediction clusteringModelPrediction = model.predictClustering(row);
                            System.out.println(currentFile.getName() + " " + config.getModel().getModelCategory().name() + " valor -> " + clusteringModelPrediction.cluster);
                            break;
                        case "Binomial":
                            BinomialModelPrediction binomialModelPrediction = model.predictBinomial(row);
                            System.out.println(currentFile.getName() + " " + config.getModel().getModelCategory().name() + " valor -> " + binomialModelPrediction.label);
                            break;
                        case "AutoEncoder":
                            AutoEncoderModelPrediction autoEncoderModelPrediction = model.predictAutoEncoder(row);
                            System.out.println(currentFile.getName() + " " + config.getModel().getModelCategory().name() + " valor -> " + autoEncoderModelPrediction.reconstructedRowData);
                            break;
                        default:
                            System.out.println("Unknown model");
                            break;
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @RequestMapping("/complain-predictions/count")
    public Integer getcomplainPredictionCount() {
        return new ComplainPrediction().getComplainPredictionsCount();
    }
}