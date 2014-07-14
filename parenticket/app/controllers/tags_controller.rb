class TagsController < ApplicationController
  def index
    @tags = Tag.all.map {|tag| tag.name}
    respond_to do |format|
      format.json { render json: @tags }
      format.html { render text: "tags#index", layout: true }
    end
  end
end
