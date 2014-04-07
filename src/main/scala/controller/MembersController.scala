package controller

import skinny._
import skinny.validator._
import _root_.controller._
import model.Member

class MembersController extends SkinnyResource with ApplicationController {
  protectFromForgery()

  override def model = Member
  override def resourcesName = "members"
  override def resourceName = "member"

  override def resourcesBasePath = s"/${toSnakeCase(resourcesName)}"
  override def useSnakeCasedParamKeys = true

  override def viewsDirectoryPath = s"/${resourcesName}"

  override def createParams = Params(params).withDate("birthday")
  override def createForm = validation(createParams,
    paramKey("name") is required & maxLength(512),
    paramKey("nickname") is required & maxLength(64),
    paramKey("birthday") is dateFormat
  )
  override def createFormStrongParameters = Seq(
    "name" -> ParamType.String,
    "nickname" -> ParamType.String,
    "birthday" -> ParamType.LocalDate
  )

  override def updateParams = Params(params).withDate("birthday")
  override def updateForm = validation(updateParams,
    paramKey("name") is required & maxLength(512),
    paramKey("nickname") is required & maxLength(64),
    paramKey("birthday") is dateFormat
  )
  override def updateFormStrongParameters = Seq(
    "name" -> ParamType.String,
    "nickname" -> ParamType.String,
    "birthday" -> ParamType.LocalDate
  )

}
