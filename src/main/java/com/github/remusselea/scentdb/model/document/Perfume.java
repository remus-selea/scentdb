package com.github.remusselea.scentdb.model.document;

import com.github.remusselea.scentdb.dto.model.perfume.Gender;
import com.github.remusselea.scentdb.dto.model.perfume.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "scentdbindex",  createIndex = true)
public class Perfume {

  @Id
  private Long perfumeId;

  @Field(type = FieldType.Text, name = "title")
  private String title;

  @Field(type = FieldType.Text, name = "brand")
  private String brand;

  @Field(type = FieldType.Integer, name = "launchYear")
  private int launchYear;

  @Field(type = FieldType.Keyword, name = "gender")
  private Gender gender;

  @Field(type = FieldType.Text, name = "perfumeType")
  private Type perfumeType;

  @Field(type = FieldType.Text, name = "bottleSizes")
  private String bottleSizes;

  @Field(type = FieldType.Text, name = "perfumer")
  private String perfumer;

  @Field(type = FieldType.Text, name = "description")
  private String description;

  @Field(type = FieldType.Text, name = "imgPath")
  private String imgPath;

}
