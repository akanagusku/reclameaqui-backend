package br.com.santander.predictbacen.reclameaqui.controller;

import br.com.santander.predictbacen.reclameaqui.model.CompanyComplainList;
import br.com.santander.predictbacen.reclameaqui.model.Complain;
import br.com.santander.predictbacen.reclameaqui.model.ComplainPrediction;
import br.com.santander.predictbacen.reclameaqui.model.PredictionModel;
import br.com.santander.predictbacen.reclameaqui.viewmodel.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

@RestController
public class ComplainController {

    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36";

    @RequestMapping("/complains")
    public ArrayList<ComplainViewModel> getComplains() {
        ArrayList<ComplainViewModel> complains = new ArrayList<>();
        Integer championId = new PredictionModel().getChampion().id;
        new Complain().getAll(championId).forEach((complain) -> {
            ComplainViewModel complainVM = new ComplainViewModel();
            complainVM.description = complain.description;
            complainVM.id = complain.id;
            complainVM.userCity = complain.userCity;
            complainVM.userState = complain.userState;
            complainVM.title = complain.title;
            complainVM.predictedScore = complain.predictedScore;
            complains.add(complainVM);
        });
        return complains;
    }

    @RequestMapping("/complain/{complainId}/prediction")
    public ArrayList<ComplainPredictionViewModel> getComplainPrediction(@PathVariable(value="complainId") String name) {
        ArrayList<ComplainPredictionViewModel> predictions = new ArrayList<>();
        new ComplainPrediction().getByComplainId(name).forEach((complainPrediction) -> {
            ComplainPredictionViewModel predictionVM = new ComplainPredictionViewModel();
            predictionVM.id = complainPrediction.id;
            predictionVM.predictedScore = complainPrediction.predictedScore;
            predictionVM.predictionModelId = complainPrediction.predictionModelId;
            predictionVM.complainId = complainPrediction.complainId;
            predictionVM.hitPercentage = complainPrediction.hitPercentage;
            predictionVM.modelName = complainPrediction.predictionModel.modelName;
            predictionVM.modelCategory = complainPrediction.predictionModel.modelCategory;
            predictions.add(predictionVM);
        });
        return predictions;
    }

    @RequestMapping("/complain/setup")
    public void setupComplains() {
        int maxPossibleComplaints = 99950;
        final int pageSize = 50;
        int requestQuantity = maxPossibleComplaints / pageSize;

        ArrayList<Integer> pageIndexes = new ArrayList<Integer>();

        for (int i = 0; i < requestQuantity; i++) {
            pageIndexes.add(i);
        }

        pageIndexes.parallelStream().map(pageIndex -> {
            try {
                String urlPattern = "https://iosearch.reclameaqui.com.br/raichu-io-site-search-v1/query/companyComplains/#PAGE_SIZE#/#START_INDEX#?company=98";
                return sendGet(urlPattern, pageIndex, pageSize);
            } catch (Exception ex) {
                return "Erro";
            }
        }).forEach(s -> {
            System.out.println(s);
            CompanyComplainList.extractComplainList(s);
        });
    }

    @RequestMapping("/complain/fetch")
    public void fetchComplains() {
        int maxPossibleComplaints = 500;
        final int pageSize = 50;
        int requestQuantity = maxPossibleComplaints / pageSize;

        ArrayList<Integer> pageIndexes = new ArrayList<Integer>();

        for (int i = 0; i < requestQuantity; i++) {
            pageIndexes.add(i);
        }

        pageIndexes.parallelStream().map(pageIndex -> {
            try {
                String urlPattern = "https://iosearch.reclameaqui.com.br/raichu-io-site-search-v1/query/companyComplains/#PAGE_SIZE#/#START_INDEX#?company=98&status=PENDING&evaluated=bool:false";
                return sendGet(urlPattern, pageIndex, pageSize);
            } catch (Exception ex) {
                return "Erro";
            }
        }).forEach(s -> {
            System.out.println(s);
            CompanyComplainList.extractComplainList(s);
        });
    }

    @RequestMapping("/complain/fetch-scores")
    public void updateComplains() {
        int maxPossibleComplaints = 50000;
        final int pageSize = 50;
        int requestQuantity = maxPossibleComplaints / pageSize;

        ArrayList<Integer> pageIndexes = new ArrayList<Integer>();

        for (int i = 0; i < requestQuantity; i++) {
            pageIndexes.add(i);
        }

        pageIndexes.parallelStream().map(pageIndex -> {
            try {
                String urlPattern = "https://iosearch.reclameaqui.com.br/raichu-io-site-search-v1/query/companyComplains/#PAGE_SIZE#/#START_INDEX#?company=98&evaluated=bool:true";
                return sendGet(urlPattern, pageIndex, pageSize);
            } catch (Exception ex) {
                return "Erro";
            }
        }).forEach(s -> {
            System.out.println(s);
            CompanyComplainList.extractComplainList(s);
        });
    }

    @RequestMapping("/complain/last-year-overview")
    public ArrayList<QuantityByMonthYear> getLastYearComplains() {
        return new Complain().getLastYearComplains();
    }

    @RequestMapping("/complain/creation-answer-date-diff")
    public ArrayList<QuantityDateDiff> getComplainsCreationDateDiff() {
        return new Complain().getComplainsQuantityDateDiff();
    }

    @RequestMapping("/complain/quantity-by-state")
    public ArrayList<QuantityByStateViewModel> getComplainsByState() {
        return new Complain().getComplainsByState();
    }

    @RequestMapping("/complains/count-without-evaluation")
    public Integer getComplainsWithoutEvaluation() {
        return new Complain().getComplainsWithoutEvaluation();
    }

    private static String sendGet(String urlPattern, int currentIndex, int pageSize) throws Exception
    {
        int startIndex = currentIndex * pageSize;
        String url = urlPattern.replace("#PAGE_SIZE#", ""+pageSize).replace("#START_INDEX#", ""+startIndex);

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
}