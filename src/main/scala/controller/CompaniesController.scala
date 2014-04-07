package controller

import skinny._
import skinny.validator._
import model.Company
import java.util.Locale
import skinny.controller.feature.RequestScopeFeature

class CompaniesController extends ApplicationController {
  protectFromForgery()

  /**
   * SkinnyModel for this resource.
   */
  def model: SkinnyModel[Long, Company] = Company

  /**
   * Resource name in the plural. This name will be used for path and directory name to locale template files.
   */
  def resourcesName = "companies"

  /**
   * Resource name in the singular. This name will be used for path and validator's prefix.
   */
  def resourceName = "company"

  /**
   * Base path.
   */
  def resourcesBasePath = s"/${toSnakeCase(resourcesName)}"

  // set resourceName/resourcesName to the request scope
  beforeAction() {
    set(RequestScopeFeature.ATTR_RESOURCES_NAME -> "items")
    set(RequestScopeFeature.ATTR_RESOURCE_NAME -> "item")
  }

  /**
   * Creates validator with prefix(resourceName).
   *
   * @param params params
   * @param validations validations
   * @param locale current locale
   * @return validator
   */
  override def validation(params: Params, validations: NewValidation*)(implicit locale: Locale = currentLocale.orNull[Locale]): MapValidator = {
    validationWithPrefix(params, resourceName, validations: _*)
  }

  /**
   * Use relative path if true. This is set as false by default.
   *
   * If you set this as true, routing will become simpler but /{resources}.xml or /{resources}.json don't work.
   */
  def useRelativePathForResourcesBasePath: Boolean = false

  /**
   * Base path prefix. (e.g. /admin/{resourcesName} )
   */
  def resourcesBasePathPrefix: String = ""

  /**
   * Normalized base path. This method should not be overridden.
   */
  final def normalizedResourcesBasePath: String = {
    resourcesBasePath.replaceFirst("^/", "").replaceFirst("/$", "")
  }

  /**
   * Outputs debug logging for passed parameters.
   *
   * @param form input form
   * @param id id if exists
   */
  def debugLoggingParameters(form: MapValidator, id: Option[Long] = None) = {
    val forLong = id.map { id => s" for [id -> ${id}]" }.getOrElse("")
    val params = form.paramMap.map { case (name, value) => s"${name} -> '${value}'" }.mkString("[", ", ", "]")
    logger.debug(s"Parameters${forLong}: ${params}")
  }

  /**
   * Outputs debug logging for permitted parameters.
   *
   * @param parameters permitted strong parameters
   * @param id id if exists
   */
  def debugLoggingPermittedParameters(parameters: PermittedStrongParameters, id: Option[Long] = None) = {
    val forLong = id.map { id => s" for [id -> ${id}]" }.getOrElse("")
    val params = parameters.params.map { case (name, (v, t)) => s"${name} -> '${v}' as ${t}" }.mkString("[", ", ", "]")
    logger.debug(s"Permitted parameters${forLong}: ${params}")
  }

  // ----------------------------
  //  Actions for this resource
  // ----------------------------

  def enablePagination: Boolean = true

  def pageSize: Int = 20

  def pageNoParamName: String = "page"

  def totalPagesAttributeName: String = "totalPages"

  /**
   * Shows a list of resource.
   *
   * GET /{resources}/
   * GET /{resources}/?pageNo=1&pageSize=10
   * GET /{resources}
   * GET /{resources}.xml
   * GET /{resources}.json
   *
   * @param format format
   * @return list of resource
   */
  def showResources()(implicit format: Format = Format.HTML): Any = withFormat(format) {
    if (enablePagination) {
      val pageNo: Int = params.getAs[Int](pageNoParamName).getOrElse(1)
      val totalCount: Long = countResources()
      val totalPages: Int = (totalCount / pageSize).toInt + (if (totalCount % pageSize == 0) 0 else 1)

      set("items", findResources(pageSize, pageNo))
      set(totalPagesAttributeName -> totalPages)
    } else {
      set("items", findResources())
    }
    render(s"/companies/index")
  }

  def countResources(): Long = model.countAllModels()

  def findResources(pageSize: Int, pageNo: Int): List[_] = model.findModels(pageSize, pageNo)

  def findResources(): List[_] = model.findAllModels()

  /**
   * Show single resource.
   *
   * GET /{resources}/{id}
   * GET /{resources}/{id}.xml
   * GET /{resources}/{id}.json
   *
   * @param id id
   * @param format format
   * @return single resource
   */
  def showResource(id: Long)(implicit format: Format = Format.HTML): Any = withFormat(format) {
    set("item", findResource(id).getOrElse(haltWithBody(404)))
    render(s"/companies/show")
  }

  def findResource(id: Long): Option[_] = model.findModel(id)

  /**
   * Shows input form for creation.
   *
   * GET /{resources}/new
   *
   * @param format format
   * @return input form
   */
  def newResource()(implicit format: Format = Format.HTML): Any = withFormat(format) {
    render(s"/companies/new")
  }

  /**
   * Params for creation.
   */
  def createParams: Params = Params(params)

  /**
   * Input form for creation
   */
  def createForm = validation(createParams,
    paramKey("name") is required & maxLength(512),
    paramKey("url") is required & maxLength(512)
  )

  /**
   * Strong parameter definitions for creation form
   */
  def createFormStrongParameters = Seq(
    "name" -> ParamType.String,
    "url" -> ParamType.String
  )

  /**
   * Creates new resource.
   *
   * POST /{resources}
   *
   * @param format format
   * @return created response if success
   */
  def createResource()(implicit format: Format = Format.HTML): Any = withFormat(format) {
    debugLoggingParameters(createForm)
    if (createForm.validate()) {
      val id = {
        val parameters = createParams.permit(createFormStrongParameters: _*)
        debugLoggingPermittedParameters(parameters)
        model.createNewModel(parameters)
      }
      format match {
        case Format.HTML =>
          flash += ("notice" -> createI18n().get(s"${resourceName}.flash.created").getOrElse(s"The ${resourceName} was created."))
          redirect302(s"/${normalizedResourcesBasePath}/${model.idToRawValue(id)}")
        case _ =>
          status = 201
          response.setHeader("Location", s"${contextPath}/${resourcesName}/${model.idToRawValue(id)}")
      }
    } else {
      status = 400
      format match {
        case Format.HTML => render(s"/companies/new")
        case _ => renderWithFormat(keyAndErrorMessages)
      }
    }
  }

  /**
   * Shows input form for modification.
   *
   * GET /{resources}/{id}/edit
   *
   * @param id id
   * @param format format
   * @return input form
   */
  def editResource(id: Long)(implicit format: Format = Format.HTML): Any = withFormat(format) {
    model.findModel(id).map { m =>
      status = 200
      format match {
        case Format.HTML =>
          setAsParams(m)
          render(s"/companies/edit")
        case _ =>
      }
    } getOrElse haltWithBody(404)
  }

  /**
   * Params for modification.
   */
  def updateParams = Params(params)

  /**
   * Input form for modification
   */
  def updateForm = validation(updateParams,
    paramKey("name") is required & maxLength(512),
    paramKey("url") is required & maxLength(512)
  )

  /**
   * Strong parameter definitions for modification form
   */
  def updateFormStrongParameters = Seq(
    "name" -> ParamType.String,
    "url" -> ParamType.String
  )

  /**
   * Updates the specified single resource.
   *
   * PUT /{resources}/{id}
   *
   * @param id id
   * @param format format
   * @return result
   */
  def updateResource(id: Long)(implicit format: Format = Format.HTML): Any = withFormat(format) {
    debugLoggingParameters(updateForm, Some(id))

    model.findModel(id).map { m =>
      if (updateForm.validate()) {
        val parameters = updateParams.permit(updateFormStrongParameters: _*)
        debugLoggingPermittedParameters(parameters, Some(id))
        model.updateModelById(id, parameters)
        status = 200
        format match {
          case Format.HTML =>
            flash += ("notice" -> createI18n().get(s"${resourceName}.flash.updated").getOrElse(s"The ${resourceName} was updated."))
            set("item", model.findModel(id).getOrElse(haltWithBody(404)))
            redirect302(s"/${normalizedResourcesBasePath}/${model.idToRawValue(id)}")
          case _ =>
        }
      } else {
        status = 400
        format match {
          case Format.HTML => render(s"/companies/edit")
          case _ => renderWithFormat(keyAndErrorMessages)
        }
      }
    } getOrElse haltWithBody(404)
  }

  /**
   * Destroys the specified resource.
   *
   * DELETE /{resources}/{id}
   *
   * @param id id
   * @param format format
   * @return result
   */
  def destroyResource(id: Long)(implicit format: Format = Format.HTML): Any = withFormat(format) {
    model.findModel(id).map { m =>
      model.deleteModelById(id)
      flash += ("notice" -> createI18n().get(s"${resourceName}.flash.deleted").getOrElse(s"The ${resourceName} was deleted."))
      status = 200
    } getOrElse haltWithBody(404)
  }

}
