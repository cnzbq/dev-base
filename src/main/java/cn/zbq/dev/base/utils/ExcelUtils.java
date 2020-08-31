package cn.zbq.dev.base.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * excel 操作工具类
 * <p>
 * 对EasyExcelFactory针对web场景做了进一步的封装
 * 官方文档{@link https://www.yuque.com/easyexcel/doc/easyexcel}
 *
 * @author Dingwq
 * @date 2020/8/30
 */
@Slf4j
@SuppressWarnings("all")
public class ExcelUtils extends EasyExcel {

    /**
     * web下载使用（Excel 2007+）
     * <p>
     * 导出的具体字段可以使用 {@link com.alibaba.excel.annotation.ExcelProperty} 配置
     *
     * @param response  {@link HttpServletResponse}
     * @param dataClass 要导出数据的对象类型
     * @param fileName  要导出的文件名
     * @param dataList  数据list
     * @param sheetName sheet名，可空
     * @param errorR    发生异常时返回的错误信息
     * @param <T>       数据范型
     */
    public static <T> void downloadForWeb(HttpServletResponse response, Class<T> dataClass, String fileName,
                                          List<T> dataList, String sheetName, R errorR) {
        if (StringUtils.isBlank(sheetName)) {
            sheetName = "sheet1";
        }
        try {
            ExcelWriterBuilder writerBuilder = getExcelWriterBuilder(response, dataClass, fileName);
            writerBuilder.autoCloseStream(Boolean.FALSE).sheet(sheetName)
                    .doWrite(dataList);
        } catch (Exception e) {
            downloadErrorHandler(response, errorR);
        }
    }

    /**
     * web下载使用（Excel 2007+），导出的sheet进行加密（不可编辑）
     * 导出的具体字段可以使用 {@link com.alibaba.excel.annotation.ExcelProperty} 配置
     *
     * @param response  {@link HttpServletResponse}
     * @param dataClass 要导出数据的对象类型
     * @param fileName  要导出的文件名
     * @param dataList  数据list
     * @param sheetName sheet名，可空
     * @param password  密码
     * @param errorR    发生异常时返回的错误信息
     * @param <T>       数据范型
     */
    public static <T> void downloadForWeb(HttpServletResponse response, Class<T> dataClass, String fileName,
                                          List<T> dataList, String sheetName, String password, R errorR) {
        if (StringUtils.isBlank(sheetName)) {
            sheetName = "sheet1";
        }
        try {
            ExcelWriterBuilder writerBuilder = getExcelWriterBuilder(response, dataClass, fileName);
            writerBuilder.password(password).autoCloseStream(Boolean.FALSE).sheet(sheetName)
                    .doWrite(dataList);
        } catch (Exception e) {
            downloadErrorHandler(response, errorR);
        }
    }

    /**
     * web下载使用（Excel 2007+）
     * <p>
     * 导出的具体字段可以使用 {@link com.alibaba.excel.annotation.ExcelProperty} 配置
     *
     * @param response        {@link HttpServletResponse}
     * @param dataClass       要导出数据的对象类型
     * @param fileName        要导出的文件名
     * @param sheetName       sheet名，可空
     * @param errorR          发生异常时返回的错误信息
     * @param getDataFunction 获取数据的函数接口 eg:(currentPage)->page(new Page<>(currentPage, 1000), Wrappers.lambdaQuery())
     * @param <T>             数据范型
     */
    public static <T> void downloadBatchForWeb(HttpServletResponse response, Class<T> dataClass, String fileName,
                                               String sheetName, R errorR, Function<Integer, Page<T>> getDataFunction) {
        if (StringUtils.isBlank(sheetName)) {
            sheetName = "sheet1";
        }
        ExcelWriter excelWriter = null;
        try {
            webResponseBase(response, fileName);
            excelWriter = EasyExcel.write(response.getOutputStream(), dataClass).build();
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();
            int currentPage = 1;
            while (true) {
                Page<T> page = getDataFunction.apply(currentPage++);
                List<T> records = page.getRecords();
                if (CollectionUtils.isEmpty(records)) {
                    break;
                }
                excelWriter.write(records, writeSheet);
            }

        } catch (Exception e) {
            downloadErrorHandler(response, errorR);
        } finally {
            if (Objects.nonNull(excelWriter)) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 下载发生异常的处理逻辑
     *
     * @param response /
     * @param errorR   /
     */
    private static void downloadErrorHandler(HttpServletResponse response, R errorR) {
        // 重置response
        response.reset();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String value = objectMapper.writeValueAsString(errorR);
            response.getWriter().println(value);
        } catch (IOException ioException) {
            log.error("模版导出返回错误结果时发生异常", ioException);
            throw new ApiException(ApiErrorCode.FAILED);
        }
    }

    /**
     * 构建基本的excel导出对象
     *
     * @param response  /
     * @param dataClass /
     * @param fileName  /
     * @param <T>       /
     * @return ExcelWriterBuilder
     * @throws IOException /
     */
    private static <T> ExcelWriterBuilder getExcelWriterBuilder(HttpServletResponse response, Class<T> dataClass,
                                                                String fileName) throws IOException {
        webResponseBase(response, fileName);
        // 这里需要设置不关闭流
        return EasyExcel.write(response.getOutputStream(), dataClass);
    }

    /**
     * web 导出信息头
     *
     * @param response /
     * @param fileName /
     * @throws UnsupportedEncodingException /
     */
    private static void webResponseBase(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
    }
}
