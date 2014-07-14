class ProjectsController < ApplicationController
  def index
    @projects = Project.all
    respond_to do |format|
      format.json { render json: @projects }
      format.html { render text: "projects#index", layout: true }
    end
  end

  def show
    @project = Project.find(params[:id])
    respond_to do |format|
      format.json { render json: @project.to_json(include: :tickets) }
      format.html { render text: "projects#show#{params[:id]}", layout: true }
    end
  end

  def create
    respond_to do |format|
      format.json do
        @project = Project.new(project_params)
        if @project.save
          render json: @project
        else
          render plain: "500", status: 500
        end
      end
    end
  end

  def update
    respond_to do |format|
      format.json do
        @project = Project.find(params[:id])
        if @project.update(project_params)
          render json: @project
        else
          render plain: "500", status: 500
        end
      end
    end
  end

  def destroy
    @project = Project.find(params[:id])
    @project.destroy
    render plain: "200 OK", status: 200
  end

  private

  def project_params
    params.require(:project).permit(:name)
  end
end
