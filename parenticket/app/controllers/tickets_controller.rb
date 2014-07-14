class TicketsController < ApplicationController

  def index
    @tickets = Ticket.where(project_id: params[:project_id])
    respond_to do |format|
      format.json { render json: @tickets }
      format.html { render text: "tickets#index", layout: true }
    end
  end

  def new
  end

  def create
    respond_to do |format|
      format.json do
        @ticket = Ticket.new(ticket_params)
        @ticket.project_id = params[:project_id]
        @ticket.status ||= 0
        @ticket.tags = (params[:tags] || []).map { |tag_name| Tag.find_or_create_by(:name => tag_name) }
        if @ticket.save
          render json: @ticket
        else
          render plain: "500", status: 500
        end
      end
    end
  end

  def show
    @ticket = Ticket.find(params[:id])
    respond_to do |format|
      format.json { render json: @ticket }
      format.html { render text: "tickets#show#{params[:id]}", layout: true }
    end
  end

  def edit
  end

  def update
    respond_to do |format|
      format.json do
        @ticket = Ticket.find(params[:id])
        @ticket.tags = (params[:tags] || []).map { |tag_name| Tag.find_or_create_by(:name => tag_name) }
        if @ticket.update(ticket_params)
          puts @ticket.inspect
          render json: @ticket
        else
          render plain: "500", status: 500
        end
      end
    end
  end

  def destroy
    @ticket = Ticket.find(params[:id])
    @ticket.destroy
    render plain: "200 OK", status: 200
  end

  private

  def ticket_params
    params.require(:ticket).permit(:name, :description, :priority, :deadline, :status)
  end
end
