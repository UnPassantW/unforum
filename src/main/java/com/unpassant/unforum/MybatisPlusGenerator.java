package com.unpassant.unforum;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;

public class MybatisPlusGenerator {
    public static void main(String[] args) {
        AutoGenerator autoGenerator = new AutoGenerator();
        DataSourceConfig datasource = new DataSourceConfig();

        datasource.setDriverName("com.mysql.cj.jdbc.Driver");
        datasource.setUrl("jdbc:mysql://localhost:3306/unforum");
        datasource.setUsername("root");
        datasource.setPassword("13155");
        autoGenerator.setDataSource(datasource);

        //全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setAuthor("UnPassant");
        globalConfig.setOutputDir("D:/Program Study/Practical_Training/MP生成器");
        globalConfig.setOpen(false);
        globalConfig.setFileOverride(false);
        autoGenerator.setGlobalConfig(globalConfig);

        //包配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.unpassant");//设置生成的包名
        packageConfig.setEntity("domain");//设置实体类包名
        packageConfig.setMapper("dao");//设置数据层包名
        autoGenerator.setPackageInfo(packageConfig);

        //策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setEntityLombokModel(true);//设置是否启用Lombok
        autoGenerator.setStrategy(strategyConfig);

        autoGenerator.execute();
    }
}
