package com.luther.miniprogrammer.service;

import com.luther.miniprogrammer.dto.ProductDto;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingService {
    @Autowired
    private CacheService cacheService;

    public void analysis(MultipartFile file) throws IOException {
        String filePath = file.getOriginalFilename();
        String extString = filePath.substring(filePath.lastIndexOf("."));
        Workbook wb = null;
        List<ProductDto> productDtoList = new ArrayList<>();

        try {
            if (".xls".equals(extString)) {
                wb = new HSSFWorkbook(file.getInputStream());
            } else if (".xlsx".equals(extString)) {
                wb = new XSSFWorkbook(file.getInputStream());
            } else {
                throw new Exception("文件格式错误");
            }
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            boolean isFirst = Boolean.FALSE;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (!isFirst) {
                    isFirst = Boolean.TRUE;
                    continue;
                }
                int cellNum = row.getLastCellNum();
                ProductDto productDto = new ProductDto();
                for (int i = 0; i <= cellNum; i++) {
                    Cell cell = row.getCell(i);
                    if (cell == null) {
                        continue;
                    }
                    CellType cellType = cell.getCellType();
                    String value = "";
                    if (cellType == CellType.NUMERIC) {
                        value = cell.getNumericCellValue() + "";
                    } else {
                        value = cell.getStringCellValue();
                    }
                    switch (i) {
                        case 0:
                            productDto.setName(value);
                            break;
                        case 1:
                            productDto.setPrice(value);
                            break;
                        case 2:
                            productDto.setDesc(value);
                            break;
                        case 3:
                            productDto.setRemark(value);
                            break;
                        case 4:
                            productDto.setTmallPrice(value);
                            break;
                        case 5:
                            productDto.setJdPrice(value);
                            break;
                    }
                }
                productDtoList.add(productDto);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            wb.close();
        }
        cacheService.updateCache(productDtoList);
    }

    public List<ProductDto> search(String searchContent){
        List<ProductDto> productDtos = cacheService.getCache();
        if (StringUtils.isEmpty(searchContent)){
            return productDtos;
        }
        return productDtos.stream().filter(dto->dto.getName().contains(searchContent)).collect(Collectors.toList());
    }
}
