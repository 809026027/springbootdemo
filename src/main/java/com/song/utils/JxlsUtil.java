package com.song.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.song.interfaces.MergedCallBack;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jxls.transformer.XLSTransformer;

/**
 * Created by 17060342 on 2019/6/4.
 */
public final class JxlsUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JxlsUtil.class);

    private static JxlsUtil jxlsUtil = null;

    /**
     * 
     */
    private JxlsUtil() {

    }

    /**
     * 
     * 功能描述: <br>
     * 〈单例方法〉
     *
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static JxlsUtil getInstance() {
        if (jxlsUtil == null) {
            return new JxlsUtil();
        }
        return jxlsUtil;
    }

    /**
     * 根据模板生成excel文件
     * 
     * @param map
     * @param filePath
     * @param targetPath
     */
    public void buildXLS(Map map, String filePath, String targetPath) {
        XLSTransformer transformer = new XLSTransformer();
        try {
            InputStream is = new FileInputStream(filePath);
            HSSFWorkbook workBook = (HSSFWorkbook) transformer.transformXLS(is, map);
            workBook.setActiveSheet(0);
            OutputStream os = new FileOutputStream(targetPath);
            workBook.write(os);
            is.close();
            os.flush();
        } catch (FileNotFoundException e) {
            LOGGER.error("buildXLS exception message: ", e);
        } catch (IOException e) {
            LOGGER.error("buildXLS exception message: ", e);
        } catch (Exception e) {
            LOGGER.error("buildXLS exception message: ", e);
        }
    }

    /**
     * 根据模板生成excel文件
     *
     * @param filePath
     * @param targetPath
     */
    public void buildMultipleSheetsXLS(String filePath, String targetPath, List objects, List newSheetNames, String beanName,
            Map beanParams, int startSheetNum, MergedCallBack mergedCallBack) {
        XLSTransformer transformer = new XLSTransformer();
        OutputStream os = null;
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            HSSFWorkbook workBook = (HSSFWorkbook) transformer.transformMultipleSheetsList(is, objects, newSheetNames, beanName,
                    beanParams, startSheetNum);
            FormulaEvaluator evaluator = workBook.getCreationHelper().createFormulaEvaluator();
            for (int sheetNum = 0; sheetNum < workBook.getNumberOfSheets(); sheetNum++) {
                Sheet sheet = workBook.getSheetAt(sheetNum);
                for (Row r : sheet) {
                    for (Cell c : r) {
                        if (c.getCellType() == Cell.CELL_TYPE_FORMULA) {
                            evaluator.evaluateFormulaCell(c);
                            c.setCellType(Cell.CELL_TYPE_NUMERIC);
                        }
                    }
                }
            }
            if (mergedCallBack != null) {
                mergedCallBack.setMegerdCall(workBook);
            }
            os = new FileOutputStream(targetPath);
            workBook.write(os);
            os.flush();
        } catch (FileNotFoundException e) {
            LOGGER.error("buildMultipleSheetsXLS exception message: ", e);
        } catch (IOException e) {
            LOGGER.error("buildMultipleSheetsXLS exception message: ", e);
        } catch (Exception e) {
            LOGGER.error("buildMultipleSheetsXLS exception message: ", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("buildMultipleSheetsXLS exception message: ", e);
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    LOGGER.error("buildMultipleSheetsXLS exception message: ", e);
                }
            }
        }
    }

    /**
     * 
     * 功能描述: <br>
     * 〈根据模板文件中的多个sheets来产生相应的xls文件。〉
     *
     * @param filePath
     * @param targetPath
     * @param listAllReportData
     * @param listTemplateSheetName
     * @param listNewSheetName
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public void buildMultipleTemplatesSheetsXLS(String filePath, String targetPath, List listAllReportData, List listTemplateSheetName,
            List listNewSheetName) {
        XLSTransformer transformer = new XLSTransformer();
        OutputStream os = null;
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            HSSFWorkbook workBook = (HSSFWorkbook) transformer.transformXLS(is, listTemplateSheetName, listNewSheetName, listAllReportData);
            FormulaEvaluator evaluator = workBook.getCreationHelper().createFormulaEvaluator();
            for (int sheetNum = 0; sheetNum < workBook.getNumberOfSheets(); sheetNum++) {
                Sheet sheet = workBook.getSheetAt(sheetNum);
                for (Row r : sheet) {
                    for (Cell c : r) {
                        if (c.getCellType() == Cell.CELL_TYPE_FORMULA) {
                            evaluator.evaluateFormulaCell(c);
                            c.setCellType(Cell.CELL_TYPE_NUMERIC);
                        }
                    }
                }
            }

            os = new FileOutputStream(targetPath);
            workBook.write(os);
            os.flush();
        } catch (FileNotFoundException e) {
            LOGGER.error("buildMultipleTemplatesSheetsXLS exception message: ", e);
        } catch (IOException e) {
            LOGGER.error("buildMultipleTemplatesSheetsXLS exception message: ", e);
        } catch (Exception e) {
            LOGGER.error("buildMultipleTemplatesSheetsXLS exception message: ", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("buildMultipleTemplatesSheetsXLS exception message: ", e);
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    LOGGER.error("buildMultipleTemplatesSheetsXLS exception message: ", e);
                }
            }
        }
    }
}
