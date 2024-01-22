package boo.ecrodrigues.user.infrastructure.configuration;

import boo.ecrodrigues.user.infrastructure.account.persistence.AccountEntity;
import com.google.common.collect.ImmutableList;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
@Import(value = MongoAutoConfiguration.class)
public class MongoConfig {

  private static final Logger logger = LoggerFactory.getLogger(MongoConfig.class);

  private static final String CREATING_COLLECTION = "Creating collection: '{}'";

  @Autowired
  private MongoDatabaseFactory mongoDbFactory;

  @Autowired
  private DatabaseCollectionsConfig collectionProperties;

  @Bean
  public MongoTransactionManager mongoTransactionManager() {
    return new MongoTransactionManager(this.mongoDbFactory);
  }

  @Bean
  public MongoTemplate mongoTemplate(final MappingMongoConverter converter, final MongoMappingContext mongoMappingContext) {
    final MongoTemplate mongoTemplate = new MongoTemplate(this.mongoDbFactory, converter);
    createCollectionsAndIndexesIfNecessary(mongoTemplate, mongoMappingContext);
    return mongoTemplate;
  }

  public void createCollectionsAndIndexesIfNecessary(final MongoTemplate mongoTemplate, final MongoMappingContext mongoMappingContext) {
    final IndexResolver resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);
    final String collection = collectionProperties.getAccount();
    if (!mongoTemplate.collectionExists(collection)) {
      logger.info(CREATING_COLLECTION, collection);
      mongoTemplate.createCollection(collection);
    }
    final IndexOperations vendorIndexOperation = mongoTemplate.indexOps(AccountEntity.class);
    resolver.resolveIndexFor(AccountEntity.class).forEach(vendorIndexOperation::ensureIndex);
  }

  @Bean
  public MappingMongoConverter mappingMongoConverter(@Qualifier("customConversions") MongoCustomConversions customConversions, final MongoMappingContext mongoMappingContext) {

    final DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
    final MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
    converter.setTypeMapper(new DefaultMongoTypeMapper(null));
    converter.setCustomConversions(customConversions);
    return converter;
  }

  @Bean
  public MongoCustomConversions customConversions() {
    return new MongoCustomConversions(ImmutableList.of(DateToOffsetDateTimeConverter.INSTANCE, OffsetDateTimeToDateConverter.INSTANCE));
  }


  @ReadingConverter
  private static final class DateToOffsetDateTimeConverter implements Converter<Date, OffsetDateTime> {

    public static final DateToOffsetDateTimeConverter INSTANCE = new DateToOffsetDateTimeConverter();

    private DateToOffsetDateTimeConverter() {
    }

    @Override
    public OffsetDateTime convert(final Date source) {
      return OffsetDateTime.ofInstant(source.toInstant(), ZoneOffset.UTC);
    }
  }

  @WritingConverter
  private static final class OffsetDateTimeToDateConverter implements Converter<OffsetDateTime, Date> {

    public static final OffsetDateTimeToDateConverter INSTANCE = new OffsetDateTimeToDateConverter();

    private OffsetDateTimeToDateConverter() {
    }

    @Override
    public Date convert(final OffsetDateTime source) {
      return Date.from(source.toInstant());
    }
  }
}
